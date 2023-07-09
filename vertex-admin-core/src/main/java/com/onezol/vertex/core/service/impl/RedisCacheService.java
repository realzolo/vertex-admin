package com.onezol.vertex.core.service.impl;

import com.onezol.vertex.core.provider.RedisCacheProvider;
import com.onezol.vertex.core.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService implements CacheService {

    private final RedisCacheProvider cacheProvider;

    @Autowired
    public RedisCacheService(RedisCacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    @Override
    public Object get(String key) {
        return cacheProvider.get(key);
    }

    @Override
    public void put(String key, Object value) {
        cacheProvider.put(key, value);
    }

    @Override
    public boolean contains(String key) {
        return cacheProvider.contains(key);
    }

    @Override
    public void evict(String key) {
        cacheProvider.evict(key);
    }
}