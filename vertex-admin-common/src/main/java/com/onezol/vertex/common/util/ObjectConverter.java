package com.onezol.vertex.common.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * 对象转换工具类
 */
public class ObjectConverter {
    private static final ModelMapper modelMapper = new ModelMapper();

    private ObjectConverter() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * 将源对象换为目标对象
     *
     * @param source     源对象
     * @param targetType 目标对象类型
     * @return 转后的目标对象
     */
    public static <S, T> T convert(S source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        return modelMapper.map(source, targetType);
    }

    /**
     * 将源对象集合换为目标对象集合
     *
     * @param source 源对象集合
     * @return 转换后的目标对象List
     */
    public static <S, T> Collection<T> convert(Collection<S> source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        if (targetType == null) {
            throw new IllegalArgumentException("Target type must not be null");
        }

        Type targetCollectionType = new TypeToken<Collection<T>>() {
        }.getType();
        return modelMapper.map(source, targetCollectionType);
    }


    /**
     * 将Map换为目标对象
     *
     * @param map        源对象Map
     * @param targetType 目标对象类型
     * @return 转换后的目标对象
     */
    public static <T> T toObject(Map<String, Object> map, Class<T> targetType) {
        if (map == null) {
            return null;
        }
        return modelMapper.map(map, targetType);
    }

    /**
     * 将源对象换为Map
     *
     * @param source 源对象
     * @return 转换后的Map
     */
    public static <S> Map<String, Object> toMap(S source) {
        if (source == null) {
            return null;
        }

        // MatchingStrategies.STANDARD: 要求属性名称必须相同, 容忍属性类型不一致
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        Map<String, Object> resultMap = new HashMap<>();
        modelMapper.map(source, resultMap);

        return resultMap;
    }
}
