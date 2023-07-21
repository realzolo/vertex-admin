package com.onezol.vertex.common.util;

import com.onezol.vertex.common.model.record.ListResultWrapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.*;

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
     * 将源对象集合换为目标对象List
     *
     * @param source 源对象集合
     * @return 转换后的目标对象List
     */
    public static <S, T> Collection<T> convert(Collection<S> source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        Collection<T> target;
        if (source instanceof List) {
            target = new ArrayList<>();
        } else if (source instanceof Set) {
            target = new HashSet<>();
        } else if (source instanceof Queue) {
            target = new LinkedList<>();
        } else {
            target = new ArrayList<>();
        }
        for (S s : source) {
            target.add(modelMapper.map(s, targetType));
        }
        return target;
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
        return modelMapper.map(source, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * 将源对象换为目标对象
     *
     * @param source 源对象
     * @param clazz  目标对象类型
     * @return 转换后的目标对象
     */
    public static <S, T> ListResultWrapper<T> convert(ListResultWrapper<S> source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        Collection<S> items = source.getItems();
        Collection<T> result = convert(items, clazz);
        return new ListResultWrapper<>(result, source.getTotal());
    }
}
