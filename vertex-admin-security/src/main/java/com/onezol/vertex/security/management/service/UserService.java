package com.onezol.vertex.security.management.service;

import com.onezol.vertex.core.common.service.GenericService;
import com.onezol.vertex.security.management.model.dto.User;
import com.onezol.vertex.security.management.model.entity.UserEntity;

public interface UserService extends GenericService<UserEntity> {
    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserById(Long id);
}
