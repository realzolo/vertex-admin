package com.onezol.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuthorize {
    /**
     * 权限标识, 支持多个, 如：admin:test:ping,admin:test:pong
     */
    String[] value() default {};

    /**
     * 权限标识, 支持多个, 如：admin:test:ping,admin:test:pong
     */
    String[] perms() default {};

    /**
     * 角色标识, 支持多个, 如：admin,user
     */
    String[] roles() default {};
}
