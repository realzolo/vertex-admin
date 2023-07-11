package com.onezol.vertex.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.common.annotation.DictDefinition;
import com.onezol.vertex.common.annotation.InsertStrategy;
import com.onezol.vertex.common.constant.enums.FieldStrategy;
import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.model.BaseEntity;
import com.onezol.vertex.common.model.BaseParam;
import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.common.util.ConditionUtils;
import com.onezol.vertex.common.util.StringUtils;
import com.onezol.vertex.core.mapper.BaseMapper;
import com.onezol.vertex.core.model.param.GenericParam;
import com.onezol.vertex.core.service.GenericService;
import com.onezol.vertex.core.util.DictUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.onezol.vertex.common.constant.CommonConstant.MAX_PAGE_SIZE;

public class GenericServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends BaseServiceImpl<M, T> implements GenericService<T> {

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return 实体
     */
    @Override
    public T queryOne(Long id) {
        return this.getById(id);
    }

    /**
     * 条件查询
     *
     * @param param 查询参数
     */
    @Override
    public ListResultWrapper<T> queryList(GenericParam param) {
        String[] fields = param.getFields();
        Integer page = param.getPage();
        Integer pageSize = param.getPageSize();
        Map<String, Map<String, Object>> condition = param.getCondition();
        String orderBy = param.getOrderBy();

        QueryWrapper<T> wrapper = new QueryWrapper<>();

        // 查询字段
        if (Objects.nonNull(fields) && fields.length > 0) {
            // 字段处理：驼峰转下划线、加上"`"防止字段名与数据库关键字冲突
            fields = Arrays.stream(fields)
                    .filter(StringUtils::isNotBlank)
                    .map(StringUtils::camelCaseToUnderline)
                    .toArray(String[]::new);
            wrapper.select(fields);
        }

        // 条件
        if (Objects.nonNull(condition) && !condition.isEmpty()) {
            ConditionUtils.withCondition(wrapper, condition);
        }

        // 排序
        if (StringUtils.isNotBlank(orderBy)) {
            String[] vars = orderBy.split(",");
            for (String var : vars) {
                String[] split = var.trim().split(" ");
                // 字段加上"`", 防止字段名与数据库关键字冲突
                String column = "`" + split[0] + "`";
                String order = split.length > 1 ? split[1] : "asc";
                wrapper.orderBy(true, "desc".equalsIgnoreCase(order), column);
            }
        }

        // 分页
        Page<T> objectPage = getPage(page, pageSize);

        // 查询
        Page<T> resultPage;
        try {
            resultPage = this.page(objectPage, wrapper);
        } catch (Exception e) {
            if (e.getMessage().contains("Cause: java.sql.SQLSyntaxErrorException: Unknown column")) {
                String column = StringUtils.getMatch(e.getMessage(), "Unknown column '(.+?)' in");
                e.printStackTrace();
                throw new BusinessException("查询失败, 无效的字段: " + column);
            }
            throw new RuntimeException(e);
        }
        List<T> records = resultPage.getRecords();
        long total = resultPage.getTotal();

        return new ListResultWrapper<>(records, total);
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @Override
    public void delete(Long id, boolean physicalDelete) {
        boolean ok = physicalDelete ? this.deleteById(id) : this.removeById(id);
        if (!ok) {
            throw new BusinessException("删除失败");
        }
    }

    /**
     * 批量删除
     *
     * @param ids            ID列表
     * @param physicalDelete 是否物理删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids, boolean physicalDelete) {
        boolean ok = physicalDelete ? this.deleteBatchByIds(ids) : this.removeByIds(Arrays.asList(ids));
        if (!ok) {
            throw new BusinessException("删除失败");
        }
    }

    /**
     * 保存/更新
     *
     * @param data 数据
     * @return 保存/更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public T save(BaseParam data) {
        Class<T> entityClass = this.entityClass;

        // 创建实例并设置字段
        T t;
        try {
            t = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(t, data);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 字段处理
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            InsertStrategy insertStrategy = field.getAnnotation(InsertStrategy.class);
            DictDefinition dictDefinition = field.getAnnotation(DictDefinition.class);

            String fieldName = field.getName();
            Object fieldValue;
            try {
                field.setAccessible(true);
                fieldValue = field.get(t);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            // 唯一性校验与逻辑删除处理(更新时无需处理)
            if (t.getId() == null && Objects.nonNull(insertStrategy)) {
                FieldStrategy[] value = insertStrategy.value();
                for (FieldStrategy strategy : value) {
                    // 校验唯一性
                    if (strategy == FieldStrategy.UNIQUE) {
                        fieldName = StringUtils.camelCaseToUnderline(fieldName);
                        Wrapper<T> wrapper = new QueryWrapper<T>().eq(fieldName, fieldValue);
                        BaseEntity[] existEntities = this.selectIgnoreLogicDelete(wrapper);
                        if (existEntities.length == 0) {
                            continue;
                        }
                        BaseEntity existEntity = existEntities[0];
                        // 存在且未被逻辑删除的数据, 则抛出异常
                        if (!existEntity.isDeleted()) {
                            throw new BusinessException("保存失败, 数据已存在");
                        }
                        // 存在且被逻辑删除的数据, 则直接物理删除(此处不直接更新是因为某些实体类的字段并不会使用UNIQUE策略，需要数据库层面的唯一性校验)
                        this.deleteById(existEntity.getId());
                    }
                }
            }

            // 字典转换
            if (Objects.nonNull(dictDefinition)) {
                String entryKey = dictDefinition.value();
//                DictValue dictvalue = dictValueService.getByValue(entryKey, fieldValue.toString());
                int code = DictUtils.getDictCode(entryKey, fieldValue.toString());
                // 反射设置字典值
                try {
                    field.setAccessible(true);
                    field.set(t, code);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // 保存
        boolean ok;
        try {
            ok = this.saveOrUpdate(t);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("保存失败，数据已存在");
        }
        if (!ok) {
            throw new BusinessException("保存失败");
        }
        return t;
    }

    /**
     * 获取分页
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return page 页码
     */
    private Page<T> getPage(Integer page, Integer pageSize) {
        if (Objects.isNull(page) || page < 1) {
            page = 1;
        }
        if (Objects.isNull(pageSize) || pageSize < 1) {
            pageSize = 10;
        }
        // 限制最大查询数量
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        return new Page<>(page, pageSize);
    }
}
