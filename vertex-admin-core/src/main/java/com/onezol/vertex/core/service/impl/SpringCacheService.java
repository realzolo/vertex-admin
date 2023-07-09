package com.onezol.vertex.core.service.impl;

import com.onezol.vertex.core.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SpringCacheService implements CacheService {

    @Override
    @Cacheable(value = "myCache", key = "#key")
    public Object get(String key) {
        // 缓存中不存在时会执行这里的逻辑
        return null;
    }

    @Override
    @CachePut(value = "myCache", key = "#key")
    public void put(String key, Object value) {
        // 将数据放入缓存
    }

    @Override
    public boolean contains(String key) {
        // 判断缓存是否包含指定的键
        return false;
    }

    @Override
    public void evict(String key) {
        // 从缓存中移除指定的键
    }
}