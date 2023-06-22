package com.onezol.platform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {

    private static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();
    private static final Jackson2JsonRedisSerializer<Object> JSON_SERIALIZER = new Jackson2JsonRedisSerializer<>(Object.class);

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // 禁用时间戳格式
        JSON_SERIALIZER.setObjectMapper(objectMapper);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(STRING_SERIALIZER);
        redisTemplate.setHashKeySerializer(STRING_SERIALIZER);

        redisTemplate.setValueSerializer(JSON_SERIALIZER);
        redisTemplate.setHashValueSerializer(JSON_SERIALIZER);

        return redisTemplate;
    }
}
