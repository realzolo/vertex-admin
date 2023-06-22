package com.onezol.platform.service;

import com.onezol.platform.model.dto.User;
import com.onezol.platform.model.entity.UserEntity;
import com.onezol.platform.model.param.UserSignupParam;

import java.util.Map;

public interface UserService extends BaseService<UserEntity> {
    /**
     * 用户注册
     *
     * @param param 注册参数
     * @return 用户信息
     */
    User signup(UserSignupParam param);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息(包含token)
     */
    Map<String, Object> signin(String username, String password);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);
}
