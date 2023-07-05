package com.onezol.platform.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ModelUtils {
    /**
     * 将源对象转换为目标对象
     *
     * @param source      源对象
     * @param targetClass 目标对象的类
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     * @return 转换后的目标对象
     */
    public static <S, T> T convert(S source, Class<T> targetClass) throws Exception {
        T target = targetClass.getDeclaredConstructor().newInstance();

        // 使用反射获取源对象和目标对象的属性列表
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = targetClass.getDeclaredFields();

        for (Field sourceField : sourceFields) {
            for (Field targetField : targetFields) {
                if (sourceField.getName().equals(targetField.getName())) {
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);

                    // 类型匹配时进行赋值
                    if (sourceField.getType().equals(targetField.getType())) {
                        targetField.set(target, sourceField.get(source));
                        break;
                    }
                    // 处理不同类型的同名属性
                    handleDifferentTypes(source, target, sourceField, targetField);
                }
            }
        }

        return target;
    }

    /**
     * 将列表（List）转换为目标类对象（T）
     *
     * @param source      源列表
     * @param targetClass 目标类的类型
     * @param <S>         源列表元素的类型
     * @param <T>         目标类类型
     * @return 转换后的目标类对象列表
     * @throws Exception 如果转换过程中发生异常
     */
    public static <S, T> List<T> convert(List<S> source, Class<T> targetClass) throws Exception {
        List<T> convertedList = new ArrayList<>();

        for (S item : source) {
            T convertedItem = convert(item, targetClass);
            convertedList.add(convertedItem);
        }

        return convertedList;
    }


    /**
     * 将Map转换为类（对象）
     *
     * @param map         要转换的Map
     * @param targetClass 目标类的类型
     * @param <T>         目标类类型
     * @return 转换后的目标类实例
     * @throws Exception 如果转换过程中发生异常
     */
    public static <T> T convert(Map<String, Object> map, Class<T> targetClass) throws Exception {
        T target = targetClass.getDeclaredConstructor().newInstance();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            Field field = getField(targetClass, fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(target, fieldValue);
            }
        }

        return target;
    }

    /**
     * 将类（对象）转换为Map
     *
     * @param source 源对象
     * @param <S>    源对象类型
     * @return 转换后的Map
     * @throws IllegalAccessException 如果在获取字段值时发生异常
     */
    public static <S> Map<String, Object> toMap(S source) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        Class<?> sourceClass = source.getClass();
        Field[] fields = sourceClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(source);
            map.put(field.getName(), value);
        }

        return map;
    }

    /**
     * 处理不同类型的同名属性
     *
     * @param source      源对象
     * @param target      目标对象
     * @param sourceField 源属性
     * @param targetField 目标属性
     */
    private static <S, T> void handleDifferentTypes(S source, T target, Field sourceField,
                                                    Field targetField) throws Exception {
        Type sourceFieldType = sourceField.getGenericType();
        Type targetFieldType = targetField.getGenericType();

        if (sourceFieldType instanceof ParameterizedType && targetFieldType instanceof ParameterizedType) {
            ParameterizedType sourceParamType = (ParameterizedType) sourceFieldType;
            ParameterizedType targetParamType = (ParameterizedType) targetFieldType;

            Type[] sourceTypeArgs = sourceParamType.getActualTypeArguments();
            Type[] targetTypeArgs = targetParamType.getActualTypeArguments();

            // 进行特定类型的转换处理
            if (sourceTypeArgs.length == 1 && targetTypeArgs.length == 1 &&
                    sourceTypeArgs[0].equals(targetTypeArgs[0]) &&
                    sourceTypeArgs[0] instanceof Class<?> &&
                    targetTypeArgs[0] instanceof Class<?>) {

                if (List.class.isAssignableFrom(sourceField.getType()) && Set.class.isAssignableFrom(targetField.getType())) {
                    convertListToSet(source, target, sourceField, targetField);
                } else if (Set.class.isAssignableFrom(sourceField.getType()) && List.class.isAssignableFrom(targetField.getType())) {
                    convertSetToList(source, target, sourceField, targetField);
                }
            }
        } else {
            // 处理基本类型和包装类型的转换
            handlePrimitiveTypeConversion(source, target, sourceField, targetField);
        }
    }

    /**
     * 将List转换为Set
     *
     * @param source      源对象
     * @param target      目标对象
     * @param sourceField 源属性
     * @param targetField 目标属性
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     */
    private static <S, T> void convertListToSet(S source, T target, Field sourceField,
                                                Field targetField)
            throws IllegalAccessException {
        List<?> sourceList = (List<?>) sourceField.get(source);

        Set<Object> targetSet = new HashSet<>(sourceList);

        targetField.set(target, targetSet);
    }

    /**
     * 将Set转换为List
     *
     * @param source      源对象
     * @param target      目标对象
     * @param sourceField 源属性
     * @param targetField 目标属性
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     */
    private static <S, T> void convertSetToList(S source, T target, Field sourceField,
                                                Field targetField) throws IllegalAccessException {
        Set<?> sourceSet = (Set<?>) sourceField.get(source);

        List<Object> targetList = new ArrayList<>(sourceSet);

        targetField.set(target, targetList);
    }


    /**
     * 处理基本类型和包装类型的转换
     *
     * @param source      源对象
     * @param target      目标对象
     * @param sourceField 源属性
     * @param targetField 目标属性
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     */
    private static <S, T> void handlePrimitiveTypeConversion(S source, T target, Field sourceField,
                                                             Field targetField)
            throws IllegalAccessException {
        if (sourceField.getType().isPrimitive() || targetField.getType().isPrimitive()) {
            Object sourceValue = sourceField.get(source);
            Object convertedValue = convertToTargetType(sourceValue, targetField.getType());
            targetField.set(target, convertedValue);
        }
    }

    /**
     * 将源属性值转换为目标类型
     *
     * @param value      源属性值
     * @param targetType 目标类型
     * @return 转换后的值
     */
    private static Object convertToTargetType(Object value, Class<?> targetType) {
        if (targetType.equals(Integer.class) || targetType.equals(int.class)) {
            return Integer.parseInt(value.toString());
        } else if (targetType.equals(Long.class) || targetType.equals(long.class)) {
            return Long.parseLong(value.toString());
        } else if (targetType.equals(Double.class) || targetType.equals(double.class)) {
            return Double.parseDouble(value.toString());
        } else if (targetType.equals(Float.class) || targetType.equals(float.class)) {
            return Float.parseFloat(value.toString());
        } else if (targetType.equals(Byte.class) || targetType.equals(byte.class)) {
            return Byte.parseByte(value.toString());
        } else if (targetType.equals(Short.class) || targetType.equals(short.class)) {
            return Short.parseShort(value.toString());
        } else if (targetType.equals(Character.class) || targetType.equals(char.class)) {
            String stringValue = value.toString();
            if (stringValue.length() == 1) {
                return stringValue.charAt(0);
            }
        } else if (targetType.equals(Boolean.class) || targetType.equals(boolean.class)) {
            return Boolean.parseBoolean(value.toString());
        } else if (targetType.equals(String.class)) {
            return value.toString();
        }

        // 其他类型的处理逻辑...

        return value;
    }

    /**
     * 根据字段名获取类中的字段
     *
     * @param clazz     类型
     * @param fieldName 字段名
     * @return 字段对象，如果不存在则返回null
     */
    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // 如果当前类没有该字段，则递归查找父类的字段
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                return getField(superclass, fieldName);
            }
        }
        return null;
    }
}
