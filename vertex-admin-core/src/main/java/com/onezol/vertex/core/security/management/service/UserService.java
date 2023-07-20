package com.onezol.vertex.core.security.management.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.base.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.security.management.common.OnlineUserManager;
import com.onezol.vertex.core.security.management.mapper.UserMapper;
import com.onezol.vertex.core.security.management.model.dto.OnlineUser;
import com.onezol.vertex.core.security.management.model.dto.User;
import com.onezol.vertex.core.security.management.model.entity.UserEntity;
import com.onezol.vertex.core.util.ModelUtils;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService extends GenericServiceImpl<UserMapper, UserEntity> {
    private final RoleService roleService;
    private final MenuService menuService;
    private final OnlineUserManager onlineUserManager;

    public UserService(RoleService roleService, MenuService menuService, OnlineUserManager onlineUserManager) {
        this.roleService = roleService;
        this.menuService = menuService;
        this.onlineUserManager = onlineUserManager;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public User getUserByUsername(String username) {
        if (username == null) {
            return null;
        }
        UserEntity userEntity = this.getOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, username));
        return wrapUser(userEntity);
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    public User getUserById(Long id) {
        if (id == null) {
            return null;
        }
        UserEntity userEntity = this.getById(id);
        return wrapUser(userEntity);
    }


    /**
     * 将 UserEntity 转换为 User
     *
     * @param userEntity UserEntity
     * @return User
     */
    private User wrapUser(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        User user = ModelUtils.convert(userEntity, User.class);
        assert user != null;
        user.setRoles(roleService.getKeysByUserId(user.getId()));
        user.setPermissions(menuService.getPermsByUserId(user.getId()));
        return user;
    }

    /**
     * 获取在线用户列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 在线用户列表
     */
    public ListResultWrapper<OnlineUser> getOnlineUsers(Long page, Long pageSize) {
        Set<OnlineUser> users = onlineUserManager.getOnlineUsers(page, pageSize);
        long count = onlineUserManager.getOnlineUserCount();
        return new ListResultWrapper<>(users, count);
    }

    /**
     * 强制用户下线
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    public boolean forceLogout(Long userId) {
        onlineUserManager.removeOnlineUser(userId);
        return true;
    }
}
