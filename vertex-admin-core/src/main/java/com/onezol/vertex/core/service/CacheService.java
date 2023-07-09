package com.onezol.vertex.core.service;

public interface CacheService {
    Object get(String key);

    void put(String key, Object value);

    boolean contains(String key);

    void evict(String key);
}
