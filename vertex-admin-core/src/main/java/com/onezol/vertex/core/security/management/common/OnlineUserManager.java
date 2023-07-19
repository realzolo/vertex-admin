package com.onezol.vertex.core.security.management.common;

import com.onezol.vertex.common.constant.RedisKey;
import com.onezol.vertex.common.util.EncryptionUtils;
import com.onezol.vertex.core.cache.RedisCache;
import com.onezol.vertex.core.security.authentication.model.UserIdentity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class OnlineUserManager {
    private final RedisCache redisCache;

    public OnlineUserManager(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 添加在线用户
     *
     * @param userIdentity   用户信息
     * @param expirationTime 过期时间(秒)
     */
    public void addOnlineUser(UserIdentity userIdentity, Integer expirationTime) {
        Assert.notNull(userIdentity, "user must not be null");
        String key = userIdentity.getKey();
        LocalDateTime loginTime = userIdentity.getLoginTime();
        long loginTimeMillis = loginTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // 存储用户信息
        redisCache.setCacheObject(RedisKey.ONLINE_USERINFO + key, userIdentity, expirationTime, TimeUnit.SECONDS);
        // 存储用户ID(key为MD5加密后的用户ID, value为登录时间)
        Map<String, Double> zSet = new HashMap<String, Double>() {{
            put(key, (double) loginTimeMillis);
        }};
        redisCache.setCacheZSet(RedisKey.ONLINE_USERID_SET, zSet);
    }

    /**
     * 移除在线用户
     *
     * @param userId 用户ID
     */
    public void removeOnlineUser(Long userId) {
        Assert.notNull(userId, "userId must not be null");
        String key = EncryptionUtils.encryptMD5(String.valueOf(userId));
        removeOnlineUser(key);
    }

    /**
     * 移除在线用户
     *
     * @param key MD5加密后的用户ID
     */
    public void removeOnlineUser(String key) {
        // 移除用户信息
        redisCache.deleteObject(RedisKey.ONLINE_USERINFO + key);
        // 移除用户ID
        redisCache.deleteZSet(RedisKey.ONLINE_USERID_SET, key);
    }


    /**
     * 分页查询在线用户
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @return 在线用户信息
     */
    public List<UserIdentity> getOnlineUsers(Long page, Long pageSize) {
        long startIndex = (page - 1) * pageSize;
        long endIndex = page * pageSize - 1;
        // 获取在线用户ID集合
        Set<Object> keySet = redisCache.getCacheZSet(RedisKey.ONLINE_USERID_SET, startIndex, endIndex);
        // 获取在线用户信息
        List<UserIdentity> users = new ArrayList<>(keySet.size());
        keySet.forEach(key -> users.add(redisCache.getCacheObject(RedisKey.ONLINE_USERINFO + key)));
        return users;
    }

    /**
     * 获取在线用户总数
     *
     * @return 在线用户总数
     */
    public Long getOnlineUserCount() {
        return redisCache.getZSetSize(RedisKey.ONLINE_USERID_SET);
    }


    /**
     * 定时同步ID_SET中有效的用户
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void removeExpiredOnlineUser() {
        // 获取在线用户ID集合
        Set<Object> keySet = redisCache.getCacheZSet(RedisKey.ONLINE_USERID_SET, 0, -1);
        // 清除过期用户
        AtomicInteger count = new AtomicInteger();
        keySet.forEach(key -> {
            UserIdentity userIdentity = redisCache.getCacheObject(RedisKey.ONLINE_USERINFO + key);
            if (userIdentity == null) {  // 用户信息不存在, 表示用户信息已过期/被删除
                redisCache.deleteZSet(RedisKey.ONLINE_USERID_SET, key);
                count.getAndIncrement();
            }
        });
        log.info("清除过期用户: {} 个", count);
    }
}
