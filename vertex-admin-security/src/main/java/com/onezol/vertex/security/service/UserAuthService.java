package com.onezol.vertex.security.service;

import com.onezol.vertex.core.common.service.BaseService;
import com.onezol.vertex.security.model.dto.UserIdentity;
import com.onezol.vertex.security.model.entity.UserEntity;

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
     * @return 当前登录用户信息
     */
    UserIdentity getCurrentUser();

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    UserIdentity getCurrentUser(String token);

}
