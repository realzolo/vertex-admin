package com.onezol.platform.annotation;

import com.onezol.platform.constant.enums.FieldStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InsertStrategy {
    FieldStrategy[] value();
}
