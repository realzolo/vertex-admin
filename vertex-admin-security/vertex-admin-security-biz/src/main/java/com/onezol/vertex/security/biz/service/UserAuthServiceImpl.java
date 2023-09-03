package com.onezol.vertex.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.constant.RedisKey;
import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.common.util.*;
import com.onezol.vertex.core.common.cache.RedisCache;
import com.onezol.vertex.security.api.mapper.UserMapper;
import com.onezol.vertex.security.api.model.UserIdentity;
import com.onezol.vertex.security.api.model.dto.User;
import com.onezol.vertex.security.api.model.entity.UserEntity;
import com.onezol.vertex.security.api.service.OnlineUserService;
import com.onezol.vertex.security.api.service.UserAuthService;
import com.onezol.vertex.security.biz.exception.LoginException;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserAuthServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserAuthService {
    private final RedisCache redisCache;
    private final AuthenticationManager authenticationManager;
    private final OnlineUserService onlineUserService;

    @Value("${spring.jwt.expiration-time}")
    private Integer expirationTime;

    public UserAuthServiceImpl(RedisCache redisCache, AuthenticationManager authenticationManager, OnlineUserService onlineUserService) {
        this.redisCache = redisCache;
        this.authenticationManager = authenticationManager;
        this.onlineUserService = onlineUserService;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public UserEntity getUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return this.getOne(
                Wrappers.<UserEntity>lambdaQuery()
                        .eq(UserEntity::getUsername, username)
        );
    }

    /**
     * 用户注册
     *
     * @param userEntity 用户信息
     */
    public void register(UserEntity userEntity) {

    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha  验证码
     */
    public Map<String, Object> login(String username, String password, String captcha) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 调用SpringSecurity的AuthenticationManager处理登录验证
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException ex) {
            if (ex instanceof BadCredentialsException) {
                throw new LoginException("用户名或密码错误");
            }
            if (ex instanceof AccountExpiredException) {
                throw new LoginException("账号已过期");
            }
            if (ex instanceof DisabledException) {
                throw new LoginException("账号已被禁用");
            }
            if (ex instanceof LockedException) {
                throw new LoginException("账号已被锁定");
            }
            throw new LoginException(ex.getMessage());
        }

        // 获取用户信息
        Object principal = authentication.getPrincipal(); // 用户身份，也就是UserDetails实现类
        UserIdentity user = (UserIdentity) principal;

        // 处理登录成功后的逻辑
        return afterLoginSuccess(user);
    }

    /**
     * 登录成功后的处理
     *
     * @param userIdentity 用户身份信息
     * @return 登录成功后的处理结果
     */
    private Map<String, Object> afterLoginSuccess(final UserIdentity userIdentity) {
        // 记录用户登录信息
        this.recordLoginDetails(userIdentity);

        // 生成token
        String subject = userIdentity.getKey();
        String token = JwtUtils.generateToken(subject);

        // 将用户信息放入缓存
        onlineUserService.addOnlineUser(userIdentity, expirationTime);

        // 处理返回信息
        UserEntity userEntity = userIdentity.getUser();
        User user = ObjectConverter.convert(userEntity, User.class);
        assert user != null;
        user.setRoles(userIdentity.getRoles());
        user.setPermissions(userIdentity.getPermissions());

        // 返回结果
        return new HashMap<String, Object>() {{
            put("user", user);
            put("jwt", new HashMap<String, Object>() {{
                put("token", token);
                put("expire", expirationTime);
            }});
        }};
    }

    /**
     * 记录用户登录信息
     *
     * @param user 用户信息
     */
    private void recordLoginDetails(final UserIdentity user) {
        LocalDateTime loginTime = LocalDateTime.now();
        String ip = NetUtils.getHostIp();
        String location = NetUtils.getRealAddressByIP(ip);
        String agentStr = RequestUtils.getRequest().getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(agentStr);
        String browser = userAgent.getBrowser().getName();
        String os = userAgent.getOperatingSystem().getName();

        user.setLoginTime(loginTime);
        user.setIp(ip);
        user.setLocation(location);
        user.setBrowser(browser);
        user.setOs(os);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    public UserIdentity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserIdentity) {
            return (UserIdentity) principal;
        }
        return null;
    }

    /**
     * 获取当前登录用户信息
     *
     * @param userId 用户ID
     * @return 当前登录用户信息
     */
    public UserIdentity getCurrentUser(Long userId) {
        if (userId == null) {
            return null;
        }
        String key = RedisKey.ONLINE_USERINFO + EncryptionUtils.encryptMD5(String.valueOf(userId));
        return redisCache.getCacheObject(key);
    }
}
