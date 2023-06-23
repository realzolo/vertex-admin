package com.onezol.platform.util;

import com.onezol.platform.annotation.DictDefinition;
import com.onezol.platform.service.DictKeyService;
import com.onezol.platform.service.DictValueService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        Objects.requireNonNull(clazz, "目标类不能为空");

        if (Objects.isNull(source)) {
            return null;
        }

        if (source instanceof Map) {
            return (T) convertMapToObject((Map) source, clazz);
        }

        T target;
        try {
            target = clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            // 如上，先判断是否有set方法，再判断是否有对应的get方法。如果类型不匹配，再进行类型转换。
            try {
                String fieldName = field.getName();
                String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method targetMethod = clazz.getMethod(methodName, field.getType());
                String sourceMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method sourceMethod = source.getClass().getMethod(sourceMethodName);
                Object value = sourceMethod.invoke(source);
                if (Objects.isNull(value)) {
                    continue;
                }
                if (!field.getType().isInstance(value)) {
                    switch (field.getType().getName()) {
                        case "java.lang.Integer":
                            value = Integer.valueOf(String.valueOf(value));
                            break;
                        case "java.lang.Long":
                            value = Long.valueOf(String.valueOf(value));
                            break;
                        case "java.lang.Boolean":
                            value = Boolean.valueOf(String.valueOf(value));
                            break;
                        case "com.onezol.platform.model.pojo.DictType":  // 字典类型
                            // 判断源对象的属性是否标注了@DictDifinition注解，如果标注了，获取value值
                            DictDefinition dictDefinition = field.getAnnotation(DictDefinition.class);
                            if (Objects.nonNull(dictDefinition)) {
                                String dictKey = dictDefinition.value().toUpperCase();
                                // TODO: 从缓存中获取字典值
                            }
                            break;
                        default:
                            break;
                    }
                }
                targetMethod.invoke(target, value);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }

        return target;
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

}
