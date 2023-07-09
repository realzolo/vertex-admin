package com.onezol.vertex.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nimbusds.jose.JOSEException;
import com.onezol.vertex.common.constant.enums.HttpStatus;
import com.onezol.vertex.common.constant.enums.LoginStatus;
import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.util.*;
import com.onezol.vertex.core.manager.AsyncManager;
import com.onezol.vertex.core.service.MailService;
import com.onezol.vertex.core.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.util.ModelUtils;
import com.onezol.vertex.security.factory.AsyncFactory;
import com.onezol.vertex.security.mapper.UserMapper;
import com.onezol.vertex.security.model.dto.User;
import com.onezol.vertex.security.model.entity.UserEntity;
import com.onezol.vertex.security.model.param.UserSignupParam;
import com.onezol.vertex.security.service.PermissionService;
import com.onezol.vertex.security.service.RoleService;
import com.onezol.vertex.security.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.onezol.vertex.common.constant.CommonConstant.P_RK_EMAIL_CODE;

@Service
public class UserServiceImpl extends GenericServiceImpl<UserMapper, UserEntity> implements UserService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private MailService mailService;

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
            AsyncManager.asyncManager().execute(AsyncFactory.recordLoginLog(username, LoginStatus.PASSWORD_ERROR));
            throw new BusinessException(HttpStatus.LOGIN_FAILURE, "用户名或密码错误");
        }
        String key = this.getKey(username, password);
        password = EncryptionUtils.encryptSha512(key);
        if (!userEntity.getPassword().equals(password)) {
            AsyncManager.asyncManager().execute(AsyncFactory.recordLoginLog(username, LoginStatus.PASSWORD_ERROR));
            throw new BusinessException(HttpStatus.LOGIN_FAILURE, "用户名或密码错误");
        }

        return afterLoginSuccess(userEntity);
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
        if (StringUtils.isAnyBlank(email, code)) {
            throw new BusinessException(HttpStatus.LOGIN_FAILURE, "邮箱或验证码不能为空");
        }
        // 校验邮箱验证码
        String emailCode = (String) redisTemplate.opsForValue().get(P_RK_EMAIL_CODE + email);
        if (emailCode == null || !emailCode.equals(code)) {
            AsyncManager.asyncManager().execute(AsyncFactory.recordLoginLog(email, LoginStatus.CAPTCHA_ERROR));
            throw new BusinessException(HttpStatus.LOGIN_FAILURE, "邮箱或验证码错误");
        }

        // 校验邮箱是否已注册
        UserEntity userEntity = this.getOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getEmail, email));
        if (userEntity == null) {
            throw new BusinessException(HttpStatus.LOGIN_FAILURE, "邮箱未注册");
        }

        return afterLoginSuccess(userEntity);
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
        User user = ModelUtils.convert(userEntity, User.class);
        assert user != null;
        user.setRoles(roleService.getKeysByUserId(user.getId()));
        user.setPermissions(permissionService.getKeysByUserId(user.getId()));
        return user;
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public User getUserById(Long id) {
        if (id == null) {
            return null;
        }
        UserEntity userEntity = this.getById(id);
        if (userEntity == null) {
            return null;
        }
        User user = ModelUtils.convert(userEntity, User.class);
        assert user != null;
        user.setRoles(roleService.getKeysByUserId(user.getId()));
        user.setPermissions(permissionService.getKeysByUserId(user.getId()));
        return user;
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     */
    @Override
    public void sendEmailCode(String email) {
        // 校验邮箱是否合法
        if (!RegexUtils.isEmail(email)) {
            throw new BusinessException("邮箱格式不正确");
        }
        // 校验邮箱验证码是否已发送
        if (Boolean.TRUE.equals(redisTemplate.hasKey(P_RK_EMAIL_CODE + email))) {
            throw new BusinessException("验证码已发送,请勿重复发送");
        }
        // 加载邮件模板
        String template;
        try {
            template = ResourceUtils.readFile("template/mail/email-code.html");
        } catch (IOException e) {
            throw new BusinessException("邮件模板加载失败,请联系管理员");
        }
        // 生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        // 发送邮件
        String content = template.replace("${code}", code);
        try {
            mailService.sendMail(email, "验证码", content);
        } catch (Exception e) {
            throw new BusinessException("邮件发送失败,请联系管理员");
        }
        // 将验证码存入Redis
        redisTemplate.opsForValue().set(P_RK_EMAIL_CODE + email, code, 5, TimeUnit.MINUTES);
    }

    /**
     * 登录成功后的操作
     *
     * @param userEntity 用户信息
     * @return 用户信息(包含token)
     */
    private HashMap<String, Object> afterLoginSuccess(final UserEntity userEntity) {
        final String username = userEntity.getUsername();
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

        AsyncManager.asyncManager().execute(AsyncFactory.recordLoginLog(username, LoginStatus.SUCCESS));

        final String finalToken = token;
        return new HashMap<String, Object>() {{
            put("user", user);
            put("jwt", new HashMap<String, Object>() {{
                put("token", finalToken);
                put("expire", JwtUtils.EXPIRE / 1000);
            }});
        }};
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
