package com.onezol.platform.aspect;

import com.onezol.platform.annotation.Validated;
import com.onezol.platform.annotation.Validator;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.onezol.platform.constant.Constant.S_CONTROLLER_NULL_RESP;

/**
 * Controller切面
 */
@Aspect
@Order(2)
@Component
public class ParamCheckAspect {

    @Pointcut("execution(public * com.onezol..controller..*.*(..))")
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

        // 执行方法
        Object proceed = joinPoint.proceed();

        // 处理结果: 如果返回值为null, 返回S_CONTROLLER_NULL_RESP, 用于ResponseBodyAdvice捕获
        return Objects.isNull(proceed) ? S_CONTROLLER_NULL_RESP : proceed;
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
                Validated validated = field.getAnnotation(Validated.class);
                if (Objects.nonNull(validated)) {
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
            throw new BusinessException(HttpStatus.BAD_REQUEST, name + "不可为空");
        }
        if (Objects.nonNull(value)) {
            String strValue = value.toString();
            // 如果value类型为字符串
            if (String.class.isAssignableFrom(value.getClass())) {
                if (validator.minLength() > 0 && strValue.length() < validator.minLength()) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, name + "长度不能小于" + validator.minLength() + "个字符");
                }
                if (validator.maxLength() > 0 && strValue.length() > validator.maxLength()) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, name + "长度不能大于" + validator.maxLength() + "个字符");
                }
            }
            // 如果value类型为数字
            if (Number.class.isAssignableFrom(value.getClass())) {
                if (validator.minValue() > 0 && Integer.parseInt(strValue) < validator.minValue()) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, name + "值不能小于" + validator.minValue());
                }
                if (validator.maxValue() > 0 && Integer.parseInt(strValue) > validator.maxValue()) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, name + "值不能大于" + validator.maxValue());
                }
            }
            // 如果value不属于基本类型或者包装类型
            if (!Number.class.isAssignableFrom(value.getClass()) && !String.class.isAssignableFrom(value.getClass())) {
                validateObject(value);
            }
            // 正则校验
            if (StringUtils.isNotBlank(validator.regex()) && !strValue.matches(validator.regex())) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, StringUtils.hasText(validator.regexTip()) ? validator.regexTip() : name + "格式不正确");
            }
            // 如果value类型为数组
            if (value.getClass().isArray()) {
                assert value instanceof Object[];
                Object[] array = (Object[]) value;
                // 校验数组长度
                if (validator.minLength() > 0 && array.length < validator.minLength()) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, name + "长度不能小于" + validator.minLength());
                }
                if (validator.maxLength() > 0 && array.length > validator.maxLength()) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, name + "长度不能大于" + validator.maxLength());
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
