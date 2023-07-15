package com.onezol.vertex.common.annotation;

import java.lang.annotation.*;

/**
 * 匿名访问不鉴权注解
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Anonymous {
}