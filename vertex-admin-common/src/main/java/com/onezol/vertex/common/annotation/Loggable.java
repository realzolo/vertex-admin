package com.onezol.vertex.common.annotation;

import java.lang.annotation.*;

/**
 * 用户操作日志注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
}
