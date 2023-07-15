package com.onezol.vertex.core.security.management.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.core.base.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.security.management.mapper.UserMapper;
import com.onezol.vertex.core.security.management.model.dto.User;
import com.onezol.vertex.core.security.management.model.entity.UserEntity;
import com.onezol.vertex.core.util.ModelUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericServiceImpl<UserMapper, UserEntity> {
    private final RoleService roleService;
    private final MenuService menuService;

    public UserService(RoleService roleService, MenuService menuService) {
        this.roleService = roleService;
        this.menuService = menuService;
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
}
