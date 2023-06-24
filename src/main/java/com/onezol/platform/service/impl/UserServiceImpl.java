package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nimbusds.jose.JOSEException;
import com.onezol.platform.constant.enums.HttpStatus;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.mapper.UserMapper;
import com.onezol.platform.model.dto.User;
import com.onezol.platform.model.entity.UserEntity;
import com.onezol.platform.model.param.UserSignupParam;
import com.onezol.platform.service.PermissionService;
import com.onezol.platform.service.RoleService;
import com.onezol.platform.service.UserService;
import com.onezol.platform.util.EncryptionUtils;
import com.onezol.platform.util.JwtUtils;
import com.onezol.platform.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.onezol.platform.constant.Constant.P_RK_USER;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 用户注册
     *
     * @param param 注册参数
     * @return 用户信息
     */
    @Override
    public User signup(UserSignupParam param) {
        if (StringUtils.isAnyBlank(param.getUsername(), param.getPassword(), param.getEmail())) {
            throw new BusinessException("username、password、email不能为空");
        }
        // 校验用户名是否已存在
        UserEntity userEntity = this.getOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, param.getUsername()));
        if (userEntity != null) {
            throw new BusinessException("用户名已存在");
        }

        // 密码加密
        String key = this.getKey(param.getUsername(), param.getPassword());
        String password = EncryptionUtils.encryptSha512(key);

        // 封装用户信息
        userEntity = new UserEntity();
        userEntity.setUsername(param.getUsername());
        userEntity.setPassword(password);
        userEntity.setEmail(param.getEmail());

        // 设置用户角色
        // TODO: 设置用户角色

        boolean ok = this.save(userEntity);
        if (!ok) {
            throw new BusinessException("注册失败");
        }

        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        return user;
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息(包含token)
     */
    @Override
    public Map<String, Object> signinByAccount(String username, String password) {
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(HttpStatus.LOGIN_FAILURE, "用户名或密码不能为空");
        }
        // 校验用户名密码
        UserEntity userEntity = this.getOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, username));
        if (userEntity == null) {
            throw new BusinessException(HttpStatus.LOGIN_FAILURE, "用户名或密码错误");
        }
        String key = this.getKey(username, password);
        password = EncryptionUtils.encryptSha512(key);
        if (!userEntity.getPassword().equals(password)) {
            throw new BusinessException(HttpStatus.LOGIN_FAILURE, "用户名或密码错误");
        }

        // 将用户信息转换为User对象
        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        user.setRoles(roleService.getKeysByUserId(user.getId()));
        user.setPermissions(permissionService.getKeysByUserId(user.getId()));

        // 生成token
        String token;
        try {
            token = JwtUtils.generateToken(username);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        // 将用户信息存入Redis
        redisTemplate.opsForValue().set(P_RK_USER + username, user, JwtUtils.EXPIRE);
        final String finalToken = token;
        return new HashMap<String, Object>() {{
            put("user", user);
            put("jwt", new HashMap<String, Object>() {{
                put("token", finalToken);
                put("expire", JwtUtils.EXPIRE);
            }});
        }};
    }

    /**
     * 通过邮箱验证码登录
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 用户信息(包含token)
     */
    @Override
    public Map<String, Object> signinByEmail(String email, String code) {
        // TODO: 通过邮箱验证码登录
        return null;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public User getUserByUsername(String username) {
        if (username == null) {
            return null;
        }
        UserEntity userEntity = this.getOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, username));
        if (userEntity == null) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        return user;
    }

    /**
     * 获取加密key
     *
     * @param username 用户名
     * @param password 密码
     * @return 加密key
     */
    private String getKey(String username, String password) {
        StringBuilder key = new StringBuilder();
        int usernameLength = username.length();
        int passwordLength = password.length();
        int length = Math.max(usernameLength, passwordLength);
        for (int i = 0; i < length; i++) {
            if (i < usernameLength) {
                key.append(username.charAt(i));
            }
            if (i < passwordLength) {
                key.append(password.charAt(i));
            }
        }
        return key.toString();
    }
}
