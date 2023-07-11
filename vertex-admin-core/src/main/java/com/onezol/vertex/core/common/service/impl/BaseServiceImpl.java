package com.onezol.vertex.core.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.onezol.vertex.core.common.mapper.BaseMapper;
import com.onezol.vertex.core.common.model.entity.BaseEntity;
import com.onezol.vertex.core.common.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 基础服务实现类
 *
 * @param <M> BaseMapper的子类
 * @param <T> BaseEntity的子类
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<M, T> implements BaseService<T> {

    /**
     * 物理删除
     *
     * @param id id
     */
    @Override
    public boolean deleteById(Serializable id) {
        String tableName = SqlHelper.table(this.currentModelClass()).getTableName();
        // 如果id是字符串，需要加上单引号
        if (id instanceof String) {
            id = "'" + id + "'";
        }
        Wrapper<T> wrapper = Wrappers.<T>lambdaQuery()
                .setEntityClass(this.currentModelClass())
                .eq(BaseEntity::getId, id);
        long count = this.baseMapper.opsForCount(tableName, wrapper);
        if (count == 0) {
            return true;
        }
        return SqlHelper.retBool(this.baseMapper.opsForDelete(tableName, wrapper));
    }

    /**
     * 批量物理删除
     *
     * @param ids id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchByIds(Serializable[] ids) {
        String tableName = SqlHelper.table(this.currentModelClass()).getTableName();
        if (ids.length == 0) {
            return false;
        }
        Wrapper<T> wrapper = Wrappers.<T>lambdaQuery()
                .setEntityClass(this.currentModelClass())
                .in(BaseEntity::getId, Arrays.asList(ids));
        long count = this.baseMapper.opsForCount(tableName, wrapper);
        if (count == 0) {
            return true;
        }
        return SqlHelper.retBool(this.baseMapper.opsForDelete(tableName, wrapper));
    }

    /**
     * 物理删除
     *
     * @param wrapper 条件构造器
     */
    @Override
    public boolean delete(Wrapper<T> wrapper) {
        String tableName = SqlHelper.table(this.currentModelClass()).getTableName();
        long count = this.baseMapper.opsForCount(tableName, wrapper);
        if (count == 0) {
            return true;
        }
        return SqlHelper.retBool(this.baseMapper.opsForDelete(tableName, wrapper));
    }

    /**
     * 查询(忽视逻辑删除)
     *
     * @param id id
     */
    @Override
    public T selectIgnoreLogicDelete(Serializable id) {
        String tableName = SqlHelper.table(this.currentModelClass()).getTableName();
        Wrapper<T> wrapper = Wrappers.<T>lambdaQuery()
                .setEntityClass(this.currentModelClass())
                .eq(BaseEntity::getId, id);
        return this.baseMapper.opsForSelect(tableName, wrapper);
    }

    /**
     * 查询(忽视逻辑删除)
     *
     * @param ids id列表
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] selectIgnoreLogicDelete(Serializable[] ids) {
        String tableName = SqlHelper.table(this.currentModelClass()).getTableName();
        if (ids.length == 0) {
            Class<T> clazz = this.currentModelClass();
            return (T[]) Array.newInstance(clazz, 0);
        }
        Wrapper<T> wrapper = Wrappers.<T>lambdaQuery()
                .setEntityClass(this.currentModelClass())
                .in(BaseEntity::getId, Arrays.asList(ids));
        return this.baseMapper.opsForSelectList(tableName, wrapper);
    }

    /**
     * 查询(忽视逻辑删除)
     *
     * @param wrapper 条件
     */
    @Override
    public T[] selectIgnoreLogicDelete(Wrapper<T> wrapper) {
        String tableName = SqlHelper.table(this.currentModelClass()).getTableName();
        return this.baseMapper.opsForSelectList(tableName, wrapper);
    }
}
