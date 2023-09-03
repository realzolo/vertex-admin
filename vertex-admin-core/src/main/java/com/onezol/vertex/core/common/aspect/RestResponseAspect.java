package com.onezol.vertex.core.common.aspect;

import com.onezol.vertex.common.annotation.RestResponse;
import com.onezol.vertex.core.common.exception.ControllerReturnNullValueException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 对控制器(Controller)接口返回的数据进行处理
 */
@Aspect
@Component
public class RestResponseAspect {
    @Pointcut("@within(com.onezol.vertex.common.annotation.RestResponse)")
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
        Object returnValue = joinPoint.proceed();

        // 获取类信息
        Object target = joinPoint.getTarget();
        Class<?> clazz = ClassUtils.getUserClass(target);

        // 处理RestResponse统一响应
        if (clazz.isAnnotationPresent(RestController.class) && clazz.isAnnotationPresent(RestResponse.class)
                && Objects.isNull(returnValue)
        ) {
            throw new ControllerReturnNullValueException();
        }

        return returnValue;
    }
}
