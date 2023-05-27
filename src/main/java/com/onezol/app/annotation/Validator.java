package com.onezol.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validator注解：用于校验参数。
 * <br/>
 *  用于设置参数的校验规则，包括：长度、正则表达式、最大值、最小值等。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validator {
    /**
     * 是否必填
     */
    boolean required() default false;

    /**
     * 最小长度
     */
    int minLength() default 0;

    /**
     * 最大长度
     */
    int maxLength() default Integer.MAX_VALUE;

    /**
     * 最小值
     */
    int minValue() default Integer.MIN_VALUE;

    /**
     * 最大值
     */
    int maxValue() default Integer.MAX_VALUE;

    /**
     * 正则表达式
     */
    String regex() default "";

    /**
     * 正则表达式错误提示
     */
    String regexTip() default "";
}
