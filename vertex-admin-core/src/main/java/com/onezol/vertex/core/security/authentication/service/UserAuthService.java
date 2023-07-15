package com.onezol.vertex.core.security.authentication.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.util.JwtUtils;
import com.onezol.vertex.common.util.NetUtils;
import com.onezol.vertex.common.util.RequestUtils;
import com.onezol.vertex.common.util.StringUtils;
import com.onezol.vertex.core.base.service.impl.BaseServiceImpl;
import com.onezol.vertex.core.cache.RedisCache;
import com.onezol.vertex.core.security.authentication.exception.LoginException;
import com.onezol.vertex.core.security.authentication.model.UserIdentity;
import com.onezol.vertex.core.security.management.mapper.UserMapper;
import com.onezol.vertex.core.security.management.model.dto.User;
import com.onezol.vertex.core.security.management.model.entity.UserEntity;
import com.onezol.vertex.core.util.ModelUtils;
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.onezol.vertex.common.constant.RedisKey.USER_PREFIX;

@Service
public class UserAuthService extends BaseServiceImpl<UserMapper, UserEntity> {
    private final RedisCache redisCache;
    private final AuthenticationManager authenticationManager;

    @Value("${spring.jwt.expiration-time}")
    private Integer expirationTime;

    public UserAuthService(RedisCache redisCache, AuthenticationManager authenticationManager) {
        this.redisCache = redisCache;
        this.authenticationManager = authenticationManager;
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
        String uuid = userIdentity.getUuid();
        String token = JwtUtils.generateToken(uuid);

        // 将用户信息放入缓存
        redisCache.setCacheObject(USER_PREFIX + uuid, userIdentity, expirationTime, TimeUnit.SECONDS);

        // 处理返回信息
        UserEntity userEntity = userIdentity.getUser();
        User user = ModelUtils.convert(userEntity, User.class);
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
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String ip = NetUtils.getHostIp();
        String location = NetUtils.getRealAddressByIP(ip);
        String agentStr = RequestUtils.getRequest().getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(agentStr);
        String browser = userAgent.getBrowser().getName();
        String os = userAgent.getOperatingSystem().getName();

        user.setUuid(uuid);
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
     * @param token token
     * @return 当前登录用户信息
     */
    public UserIdentity getCurrentUser(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return redisCache.getCacheObject(USER_PREFIX + token);
    }
}
