package com.onezol.vertex.security.service.impl;

import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.security.model.dto.UserIdentity;
import com.onezol.vertex.security.model.entity.UserEntity;
import com.onezol.vertex.security.service.PermissionService;
import com.onezol.vertex.security.service.RoleService;
import com.onezol.vertex.security.service.UserAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsPasswordService {
    public static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserAuthService userAuthService;
    private final PermissionService permissionService;
    private final RoleService roleService;

    @Autowired
    public UserDetailsServiceImpl(@Lazy UserAuthService userAuthService, PermissionService permissionService, RoleService roleService) {
        this.userAuthService = userAuthService;
        this.permissionService = permissionService;
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
            throw new BusinessException("用户 [" + username + "] 不存在");
        }

        UserIdentity user = new UserIdentity(userEntity);
        Set<String> roles = roleService.getKeysByUserId(userEntity.getId());
        Set<String> perms = permissionService.getKeysByUserId(userEntity.getId());
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
        return user;
    }

}
