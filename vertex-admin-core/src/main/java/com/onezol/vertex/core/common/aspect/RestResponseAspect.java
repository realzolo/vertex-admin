package com.onezol.vertex.core.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.onezol.vertex.common.constant.Constants.SYMBOL_NULL;

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
        Object proceed = joinPoint.proceed();

        // 处理结果: 如果controller返回值为null, 则替换为一个特定值, 用于ResponseBodyAdvice捕获
        return Objects.isNull(proceed) ? SYMBOL_NULL : proceed;
    }
}
