package com.onezol.vertex.security.biz.service;

import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.security.api.model.UserIdentity;
import com.onezol.vertex.security.api.model.entity.UserEntity;
import com.onezol.vertex.security.api.service.MenuService;
import com.onezol.vertex.security.api.service.RoleService;
import com.onezol.vertex.security.api.service.SecurityUserDetailsService;
import com.onezol.vertex.security.api.service.UserAuthService;
import com.onezol.vertex.security.biz.exception.LoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class SecurityUserDetailsServiceImpl implements SecurityUserDetailsService {
    private final UserAuthService userAuthService;
    private final MenuService menuService;
    private final RoleService roleService;

    @Autowired
    public SecurityUserDetailsServiceImpl(@Lazy UserAuthService userAuthService, MenuService menuService, RoleService roleService) {
        this.userAuthService = userAuthService;
        this.menuService = menuService;
        this.roleService = roleService;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userAuthService.getUserByUsername(username);
        if (userEntity == null) {
            throw new LoginException("用户名或密码错误");
        }

        UserIdentity user = new UserIdentity(userEntity);
        Set<String> roles = roleService.getKeysByUserId(userEntity.getId());
        Set<String> perms = menuService.getPermsByUserId(userEntity.getId());
        user.setRoles(roles);
        user.setPermissions(perms);

        return user;
    }

    /**
     * 密码升级
     *
     * @param user        用户信息
     * @param newPassword 新密码
     * @return 用户信息
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        throw new BusinessException("密码升级功能暂未实现");
    }
}
