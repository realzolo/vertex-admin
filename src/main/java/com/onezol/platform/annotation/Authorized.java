package com.onezol.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authorized {
    /**
     * 权限key(支持单个权限和多个权限，单个权限时，直接写权限名称，多个权限时，用逗号分隔)
     */
    String value();
}
