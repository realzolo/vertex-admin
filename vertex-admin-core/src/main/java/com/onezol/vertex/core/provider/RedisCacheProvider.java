package com.onezol.vertex.core.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheProvider {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public RedisCacheProvider(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public boolean contains(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void evict(String key) {
        redisTemplate.delete(key);
    }
}
