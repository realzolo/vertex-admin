package com.onezol.platform.util;

import com.onezol.platform.model.pojo.ListResultWrapper;
import com.onezol.platform.service.DictKeyService;
import com.onezol.platform.service.DictValueService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ConvertUtils {
    private static DictKeyService dictKeyService;
    private static DictValueService dictValueService;

    public ConvertUtils(DictKeyService dictKeyService, DictValueService dictValueService) {
        ConvertUtils.dictKeyService = dictKeyService;
        ConvertUtils.dictValueService = dictValueService;
    }

    /**
     * 将给定源对象转换为目标类对象
     *
     * @param source 源对象
     * @param clazz  目标对象的类
     * @return 转换后的目标对象
     */
    public static <S, T> T convertTo(S source, Class<T> clazz) {
        T t;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        org.springframework.beans.BeanUtils.copyProperties(source, t);
        return t;
    }

    /**
     * 将给定源对象列表转换为目标类对象列表
     *
     * @param source 源对象列表
     * @param clazz  目标对象的类
     * @param <S>    源对象类型
     * @param <T>    目标对象类型
     * @return 转换后的目标对象列表
     */
    public static <S, T> List<T> convertTo(List<S> source, Class<T> clazz) {
        int size = source.size();
        List<T> target = new ArrayList<>(size);
        for (S s : source) {
            target.add(convertTo(s, clazz));
        }
        if (target.size() != size) {
            throw new RuntimeException("转换异常");
        }
        return target;
    }

    /**
     * 将给定Map对象转换为目标类对象
     *
     * @param map   源对象Map
     * @param clazz 目标对象的类
     * @param <T>   目标对象类型
     * @return 转换后的目标对象
     */
    public static <T> T convertMapToObject(Map<String, ?> map, Class<T> clazz) {
        Objects.requireNonNull(clazz, "目标类不能为空");
        T t;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.populate(t, map);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    /**
     * 将给定源对象列表转换为目标类对象列表
     *
     * @param source 源对象列表
     * @param clazz  目标对象的类
     */
    public static <S, T> ListResultWrapper<T> convertTo(ListResultWrapper<S> source, Class<T> clazz) {
        List<S> list = source.getItems();
        List<T> target = new ArrayList<>(list.size());
        for (S s : list) {
            target.add(convertTo(s, clazz));
        }
        return new ListResultWrapper<>(target, source.getTotal());
    }
}
