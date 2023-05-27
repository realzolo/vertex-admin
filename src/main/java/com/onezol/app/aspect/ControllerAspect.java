package com.onezol.app.aspect;

import com.onezol.app.annotation.Valid;
import com.onezol.app.annotation.Validator;
import com.onezol.app.constant.HttpStatus;
import com.onezol.app.exception.BusinessException;
import com.onezol.app.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Controller切面
 */
@Aspect
@Component
public class ControllerAspect {
    @Pointcut("execution(* com.onezol.app.controller..*.*(..))")
    public void pointcut() {
    }

    /**
     * @param joinPoint 切点
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 校验请求参数
        validateRequestParams(joinPoint.getArgs());

        return joinPoint.proceed();
    }

    /**
     * 校验请求参数
     *
     * @param args 请求参数
     * @throws IllegalAccessException IllegalAccessException
     */
    private void validateRequestParams(Object[] args) throws IllegalAccessException {
        for (Object arg : args) {
            Class<?> argClass = arg.getClass();
            Field[] fields = argClass.getDeclaredFields();
            for (Field field : fields) {
                // 1. 如果参数上有 @Validator 注解，直接校验这个字段
                Validator validator = field.getAnnotation(Validator.class);
                if (Objects.nonNull(validator)) {
                    validateField(field, arg);
                    break;
                }
                // 2. 如果参数上有 @Valid 注解，表示这个字段是一个对象，需要校验这个对象的所有字段
                Valid valid = field.getAnnotation(Valid.class);
                if (Objects.nonNull(valid)) {
                    field.setAccessible(true);
                    Object object = field.get(arg);
                    if (Objects.nonNull(object)) {
                        validateObject(object);
                    }
                }
            }
        }
    }

    /**
     * 校验字段
     *
     * @param field 字段
     * @param arg   参数
     * @throws IllegalAccessException IllegalAccessException
     */
    private void validateField(Field field, Object arg) throws IllegalAccessException {
        Validator validator = field.getAnnotation(Validator.class);
        if (Objects.isNull(validator)) {
            return;
        }
        field.setAccessible(true);
        Object value = field.get(arg);
        String name = field.getName();
        if (validator.required() && Objects.isNull(value)) {
            throw new BusinessException(HttpStatus.BAD_PARAMETER, name + "不可为空");
        }
        if (Objects.nonNull(value)) {
            String strValue = value.toString();
            // 如果value类型为字符串
            if (String.class.isAssignableFrom(value.getClass())) {
                if (validator.minLength() > 0 && strValue.length() < validator.minLength()) {
                    throw new BusinessException(HttpStatus.BAD_PARAMETER, name + "长度不能小于" + validator.minLength() + "个字符");
                }
                if (validator.maxLength() > 0 && strValue.length() > validator.maxLength()) {
                    throw new BusinessException(HttpStatus.BAD_PARAMETER, name + "长度不能大于" + validator.maxLength() + "个字符");
                }
            }
            // 如果value类型为数字
            if (Number.class.isAssignableFrom(value.getClass())) {
                if (validator.minValue() > 0 && Integer.parseInt(strValue) < validator.minValue()) {
                    throw new BusinessException(HttpStatus.BAD_PARAMETER, name + "值不能小于" + validator.minValue() + "");
                }
                if (validator.maxValue() > 0 && Integer.parseInt(strValue) > validator.maxValue()) {
                    throw new BusinessException(HttpStatus.BAD_PARAMETER, name + "值不能大于" + validator.maxValue() + "");
                }
            }
            // 如果value不属于基本类型或者包装类型
            if (!Number.class.isAssignableFrom(value.getClass()) && !String.class.isAssignableFrom(value.getClass())) {
                validateObject(value);
            }
            // 正则校验
            if (StringUtils.isNotBlank(validator.regex()) && !strValue.matches(validator.regex())) {
                throw new BusinessException(HttpStatus.BAD_PARAMETER, StringUtils.hasText(validator.regexTip()) ? validator.regexTip() : name + "格式不正确");
            }
            // 如果value类型为数组
            if (value.getClass().isArray()) {
                assert value instanceof Object[];
                Object[] array = (Object[]) value;
                // 校验数组长度
                if (validator.minLength() > 0 && array.length < validator.minLength()) {
                    throw new BusinessException(HttpStatus.BAD_PARAMETER, name + "长度不能小于" + validator.minLength());
                }
                if (validator.maxLength() > 0 && array.length > validator.maxLength()) {
                    throw new BusinessException(HttpStatus.BAD_PARAMETER, name + "长度不能大于" + validator.maxLength());
                }
            }
        }
    }

    /**
     * 校验对象
     *
     * @param object 对象
     * @throws IllegalAccessException IllegalAccessException
     */
    private void validateObject(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Validator validator = field.getAnnotation(Validator.class);
            if (Objects.nonNull(validator)) {
                validateField(field, object);
            }
        }
    }
}
