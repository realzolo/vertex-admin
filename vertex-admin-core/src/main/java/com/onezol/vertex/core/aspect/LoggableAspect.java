package com.onezol.vertex.core.aspect;

import com.onezol.vertex.common.annotation.Loggable;
import com.onezol.vertex.common.util.JsonUtils;
import com.onezol.vertex.common.util.RequestUtils;
import com.onezol.vertex.core.manager.async.AsyncTaskManager;
import com.onezol.vertex.core.module.log.model.entity.AccessLogEntity;
import com.onezol.vertex.core.module.log.service.AccessLogService;
import com.onezol.vertex.core.security.authentication.model.UserIdentity;
import com.onezol.vertex.core.security.management.model.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class LoggableAspect implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LoggableAspect.class);
    public final AccessLogService accessLogService;

    @Value("${vertex.log.access-log.enable:true}")
    private boolean enable;

    public LoggableAspect(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (enable) {
            logger.info("访问日志服务已开启, 如需关闭请设置 vertex.log.access-log.enable=false");
        } else {
            logger.info("访问日志服务暂未开启, 如需开启请设置 vertex.log.access-log.enable=true");
        }
    }

    @Pointcut(
            "@within(com.onezol.vertex.common.annotation.Loggable)"
    )
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 判断是否开启访问日志
        if (!enable) {
            return joinPoint.proceed();
        }

        Object result = null;
        Exception exception = null;
        long start = System.currentTimeMillis();  // 开始时间
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            exception = e;
        }
        long end = System.currentTimeMillis();    // 结束时间
        long time = end - start;                  // 耗时(单位：毫秒)

        // 处理日志
        handleLog(joinPoint, time, exception);

        if (exception != null) {
            throw exception;
        }
        return result;
    }

    /**
     * 处理日志
     *
     * @param joinPoint 切点
     * @param time      耗时(单位：毫秒)
     * @param e         异常
     */
    private void handleLog(final JoinPoint joinPoint, final long time, final Exception e) {
        if (!isLoggable(joinPoint)) {
            return;
        }
        try {
            this.submitLog(joinPoint, time, e);
        } catch (Exception exp) {
            logger.error("记录访问日志失败", exp);
            exp.printStackTrace();
        }
    }

    /**
     * 提交存储操作日志
     *
     * @param joinPoint 切点
     * @param time      耗时(单位：毫秒)
     * @param e         异常
     */
    private void submitLog(final JoinPoint joinPoint, final long time, final Throwable e) {
        HttpServletRequest request = RequestUtils.getRequest();

        //设置用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return;
        }
        UserIdentity userIdentity = (UserIdentity) authentication.getPrincipal();
        UserEntity user = userIdentity.getUser();
        if (Objects.isNull(user)) {
            return;
        }

        // 请求参数
        Object[] args = joinPoint.getArgs();
        String params = JsonUtils.toJson(args);
        // 类名
        String className = joinPoint.getTarget().getClass().getName();
        // 方法名
        String methodName = joinPoint.getSignature().getName();
        String operateMethod = className + "." + methodName;
        // 请求地址
        String url = request.getRequestURI();

        // 获取异常信息
        String failureReason = "";
        boolean isSuccess = true;
        if (Objects.nonNull(e)) {
            isSuccess = false;
            failureReason = getExceptionInfo(e);
        }

        AccessLogEntity accessLog = new AccessLogEntity();
        accessLog.setUserId(user.getId());
        accessLog.setUserName(user.getName());
        accessLog.setUserType("-");
        accessLog.setUrl(url);
        accessLog.setMethod(operateMethod);
        accessLog.setParams(params);
        accessLog.setIp(userIdentity.getIp());
        accessLog.setLocation(userIdentity.getLocation());
        accessLog.setBrowser(userIdentity.getBrowser());
        accessLog.setOs(userIdentity.getOs());
        accessLog.setSuccess(isSuccess);
        accessLog.setFailureReason(failureReason);
        accessLog.setTime(time);

        Operation operation = this.getOperation(joinPoint);
        if (Objects.nonNull(operation)) {
            // 当hidden为true时，不记录日志
            if (operation.hidden()) {
                return;
            }
            // 操作行为
            accessLog.setAction(operation.summary());
            // 操作描述
            accessLog.setDescription(operation.description());
        }

        Tag tag = this.getTag(joinPoint);
        if (Objects.nonNull(tag)) {
            // 模块
            String module = tag.name();
            accessLog.setModule(module);
        }

        AsyncTaskManager.getInstance().execute(() -> {
            this.saveLog(accessLog);
        });
    }

    /**
     * 判断是否需要记录日志
     */
    private boolean isLoggable(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return AnnotationUtils.findAnnotation(method.getDeclaringClass(), Loggable.class) != null;
    }

    /**
     * 获取异常信息字符串
     *
     * @param e 异常
     * @return 异常信息字符串
     */
    private String getExceptionInfo(Throwable e) {
        return e.getMessage();
    }

    /**
     * 获取Operation注解
     *
     * @param joinPoint 切点
     * @return Operation注解
     */
    private Operation getOperation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method.getAnnotation(Operation.class);
    }

    /**
     * 获取Tag注解
     *
     * @param joinPoint 切点
     * @return Tag注解
     */
    private Tag getTag(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return AnnotationUtils.findAnnotation(method.getDeclaringClass(), Tag.class);
    }

    /**
     * 保存日志到数据库
     *
     * @param accessLogEntity 操作日志实体
     */
    private void saveLog(AccessLogEntity accessLogEntity) {
        accessLogService.save(accessLogEntity);
    }
}
