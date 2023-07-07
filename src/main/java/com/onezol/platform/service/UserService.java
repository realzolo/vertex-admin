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
     * 通过用户名密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息(包含token)
     */
    Map<String, Object> signinByAccount(String username, String password);

    /**
     * 通过邮箱验证码登录
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 用户信息(包含token)
     */
    Map<String, Object> signinByEmail(String email, String code);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     */
    void sendEmailCode(String email);
}
