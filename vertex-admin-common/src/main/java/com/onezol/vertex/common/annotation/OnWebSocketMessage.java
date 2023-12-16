package com.onezol.vertex.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnWebSocketMessage {
    /**
     * 消息组
     */
    String[] group() default {};

    /**
     * 消息类型
     */
    String[] type() default {};
}