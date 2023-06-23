package com.onezol.platform.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.onezol.platform.annotation.DictDefinition;
import com.onezol.platform.util.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.onezol.platform.constant.Constant.RK_DICT;

@Configuration
public class MyBatisPlusConfig implements MetaObjectHandler {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());

        // 处理字典值
//        this.fillDictValue(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }

    /**
     * 处理字典值
     *
     * @param metaObject 元对象
     */
    private void fillDictValue(MetaObject metaObject) {
        // 获取所有字段名
        List<String> fieldNames = getFieldNames(metaObject);

        for (String fieldName : fieldNames) {
            // 判断字段是否有@DictDefinition注解
            DictDefinition dictDefinition = getFieldAnnotation(metaObject, fieldName, DictDefinition.class);
            if (dictDefinition != null) {
                // 获取注解上的字典键
                String dictKey = dictDefinition.value();
                if (StringUtils.isNotBlank(dictKey)) {
                    Object o = redisTemplate.opsForHash().get(RK_DICT, dictKey);
                    if (o == null) {
                        continue;
                    }
                    Integer code = (Integer) ((LinkedHashMap) o).get("code");
                    // 如果字段为null，则设置默认值
                    Object fieldValue = getFieldValByName(fieldName, metaObject);
//                    if (fieldValue == null) {
//                        setFieldValByName(fieldName, defaultValue, metaObject);
//                    }
                }
            }
        }
    }

    /**
     * 获取所有字段名
     *
     * @param metaObject 元对象
     * @return 字段名列表
     */
    private List<String> getFieldNames(MetaObject metaObject) {
        return Arrays.stream(metaObject.getOriginalObject().getClass().getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    /**
     * 获取字段上的注解
     *
     * @param metaObject      元对象
     * @param fieldName       字段名
     * @param annotationClass 注解类
     * @param <T>             注解类型
     * @return 注解
     */
    private <T extends Annotation> T getFieldAnnotation(MetaObject metaObject, String fieldName, Class<T> annotationClass) {
        try {
            Field field = metaObject.getOriginalObject().getClass().getDeclaredField(fieldName);
            return field.getAnnotation(annotationClass);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
