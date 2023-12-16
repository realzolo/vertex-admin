package com.onezol.vertex.security.api.service;

import com.onezol.vertex.common.service.BaseService;
import com.onezol.vertex.security.api.model.UserIdentity;
import com.onezol.vertex.security.api.model.entity.UserEntity;

import java.util.Map;

public interface UserAuthService extends BaseService<UserEntity> {
    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserEntity getUserByUsername(String username);

    /**
     * 用户注册
     *
     * @param userEntity 用户信息
     */
    void register(UserEntity userEntity);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha  验证码
     */
    Map<String, Object> login(String username, String password, String captcha);

    /**
     * 获取当前登录用户信息
     *
     * @param userId 用户ID
     * @return 当前登录用户信息
     */
    UserIdentity getCurrentUser(Long userId);
}
