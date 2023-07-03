package com.onezol.platform.aspect;

import com.onezol.platform.annotation.PreAuthorize;
import com.onezol.platform.annotation.Validated;
import com.onezol.platform.annotation.Validator;
import com.onezol.platform.common.UserContextHolder;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.model.dto.User;
import com.onezol.platform.service.PermissionService;
import com.onezol.platform.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Controller切面
 */
@Aspect
@Order(1)
@Component
public class AuthorizeAspect {
    @Autowired
    private PermissionService permissionService;

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
        // 校验权限
        validateAuthority(joinPoint.getSignature());

        return joinPoint.proceed();
    }

    /**
     * 校验权限
     *
     * @param signature signature
     */
    private void validateAuthority(Signature signature) {
        // 获取自定义注解中的值
        Method method = ((MethodSignature) signature).getMethod();
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
        if (Objects.isNull(annotation)) {
            return;
        }

        // controller: 权限标识
        Set<String> permKeys = new HashSet<>();
        permKeys.addAll(Arrays.asList(annotation.value()));
        permKeys.addAll(Arrays.asList(annotation.perms()));

        // controller: 角色标识
        List<String> roles = Arrays.asList(annotation.roles());

        // 用户的角色/权限
        User user = UserContextHolder.getUser();
        if (Objects.isNull(user)) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "未登录");
        }
        Set<String> userRoles = user.getRoles();
        Set<String> userPerms = user.getPermissions();
        Set<String> userPermKeys = getUserPermKeys(userRoles, userPerms);

        // 当perms为空, roles不为空时, 将roles转换为perms
        if (permKeys.size() == 0) {
            roles.stream().map(role -> role + ":*:*").forEach(permKeys::add);
        }

        // 校验权限
        doValidatePerms(permKeys, userPermKeys);
    }

    /**
     * 获取用户的所有权限
     *
     * @param userRoles 用户角色
     * @param userPerms 用户权限
     */
    private Set<String> getUserPermKeys(Set<String> userRoles, Set<String> userPerms) {
        List<String> permKeys = new ArrayList<>();
        for (String role : userRoles) {
            for (String perm : userPerms) {
                permKeys.add(role + ":" + perm);
            }
        }
        return new HashSet<>(permKeys);
    }

    /**
     * 校验权限
     *
     * @param permKeys     权限标识
     * @param userPermKeys 用户权限标识
     */
    private void doValidatePerms(Set<String> permKeys, Set<String> userPermKeys) {
        // 判断是否存在交集
        boolean hasIntersection = !Collections.disjoint(permKeys, userPermKeys);

        if (!hasIntersection) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "无权限");
        }
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
