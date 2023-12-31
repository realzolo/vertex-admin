package com.onezol.vertex.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.onezol.vertex.common.exception.BusinessException;

import java.util.Map;
import java.util.Objects;

public class ConditionUtils {

    /**
     * 解析condition
     *
     * @param queryWrapper 查询条件
     * @param condition    条件
     */
    public static void withCondition(QueryWrapper<?> queryWrapper, Map<String, Map<String, Object>> condition) {
        Objects.requireNonNull(queryWrapper, "queryWrapper不能为空");
        if (condition == null || condition.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Map<String, Object>> entry : condition.entrySet()) {
            String operator = entry.getKey();
            Map<String, Object> entryValue = entry.getValue();
            // 如果key或者field或者value为空，则抛出异常
            if (StringUtils.isBlank(operator) || entryValue == null) {
                throw new BusinessException("查询条件解析失败");
            }
            if (entryValue.isEmpty()) {
                continue;
            }
            for (Map.Entry<String, Object> objectEntry : entryValue.entrySet()) {
                String field = objectEntry.getKey();
                Object value = objectEntry.getValue();
                // 如果field或者value为空，则抛出异常
                if (StringUtils.isBlank(field) || value == null) {
                    throw new BusinessException("查询条件解析失败");
                }
                // 将驼峰转为下划线
                field = StringUtils.camelCaseToUnderline(field);
                // 将字段名加上``, 防止和数据库关键字冲突
                field = String.format("`%s`", field);
                switch (operator) {
                    case "eq":
                        queryWrapper.eq(field, value);
                        break;
                    case "ne":
                        queryWrapper.ne(field, value);
                        break;
                    case "gt":
                        queryWrapper.gt(field, value);
                        break;
                    case "ge":
                        queryWrapper.ge(field, value);
                        break;
                    case "lt":
                        queryWrapper.lt(field, value);
                        break;
                    case "le":
                        queryWrapper.le(field, value);
                        break;
                    case "like":
                        queryWrapper.like(field, value);
                        break;
                    case "notLike":
                        queryWrapper.notLike(field, value);
                        break;
                    case "in":
                        verifyIterableValue(field, value);
                        queryWrapper.in(field, value);
                        break;
                    case "notIn":
                        verifyIterableValue(field, value);
                        queryWrapper.notIn(field, value);
                        break;
                    case "isNull":
                        queryWrapper.isNull(field);
                        break;
                    case "isNotNull":
                        queryWrapper.isNotNull(field);
                        break;
                    default:
                        throw new BusinessException("condition中的key只能是eq、ne、gt、ge、lt、le、like、notLike、in、notIn、isNull、isNotNull");
                }
            }
        }
    }

    /**
     * 校验value是否是集合或者数组
     *
     * @param field 字段
     * @param value 值
     */
    private static void verifyIterableValue(String field, Object value) {
        // 判断是否是集合/数组
        if (!(value instanceof Iterable) && !(value.getClass().isArray())) {
            throw new BusinessException(String.format("condition校验失败，%s的值必须是集合或者数组", field));
        }
    }
}
