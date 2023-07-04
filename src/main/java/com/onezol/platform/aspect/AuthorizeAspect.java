package com.onezol.platform.aspect;

import com.onezol.platform.annotation.PreAuthorize;
import com.onezol.platform.common.UserContextHolder;
import com.onezol.platform.constant.enums.HttpStatus;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.model.dto.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Controller权限校验切面
 */
@Aspect
@Order(1)
@Component
public class AuthorizeAspect {

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
            throw new BusinessException(HttpStatus.UNAUTHORIZED);
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
     * @param permKeys     权限标识集合
     * @param userPermKeys 用户权限标识集合
     */
    private void doValidatePerms(Set<String> permKeys, Set<String> userPermKeys) {
        // 遍历每个权限标识
        for (String permKey : permKeys) {
            if (isPermissionGranted(permKey, userPermKeys)) {
                return; // 权限通过，退出方法
            }
        }

        throw new BusinessException(HttpStatus.NO_PERMISSION, "您没有此接口的访问权限");
    }

    /**
     * 判断是否有权限
     *
     * @param permKey      权限标识
     * @param userPermKeys 用户权限标识集合
     * @return 是否有权限
     */
    private boolean isPermissionGranted(String permKey, Set<String> userPermKeys) {
        // 遍历用户权限标识集合
        for (String userPermKey : userPermKeys) {
            if (isWildcardMatch(permKey, userPermKey)) {
                return true; // 权限匹配，返回true
            }
        }
        return false; // 权限不匹配
    }

    /**
     * 判断是否满足通配符匹配条件
     *
     * @param permKey     权限标识
     * @param userPermKey 用户权限标识
     * @return 是否满足通配符匹配条件
     */
    private boolean isWildcardMatch(String permKey, String userPermKey) {
        String[] permKeyParts = permKey.split(":"); // 拆分权限标识为三个部分
        String[] userPermKeyParts = userPermKey.split(":"); // 拆分用户权限标识为三个部分

        if (permKeyParts.length != 3 || userPermKeyParts.length != 3) {
            return false; // 权限标识格式错误，不匹配
        }

        // 逐一比较每个位置上的角色是否匹配
        for (int i = 0; i < 3; i++) {
            if (!permKeyParts[i].equals("*") && !permKeyParts[i].equals(userPermKeyParts[i])) {
                return false; // 不是通配符匹配
            }
        }

        return true; // 是通配符匹配
    }
}
