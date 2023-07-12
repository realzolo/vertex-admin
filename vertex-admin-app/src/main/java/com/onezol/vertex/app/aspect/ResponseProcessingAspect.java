package com.onezol.vertex.app.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.onezol.vertex.common.constant.Constants.CONTROLLER_NULL_RESP;

/**
 * 对控制器(Controller)接口返回的数据进行处理
 */
@Aspect
@Component
public class ResponseProcessingAspect {

    @Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut() {
    }

    /**
     * @param joinPoint 切点
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行方法
        Object proceed = joinPoint.proceed();

        // 处理结果: 如果返回值为null, 返回S_CONTROLLER_NULL_RESP, 用于ResponseBodyAdvice捕获
        return Objects.isNull(proceed) ? CONTROLLER_NULL_RESP : proceed;
    }
}
