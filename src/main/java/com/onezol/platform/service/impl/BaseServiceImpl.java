package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.onezol.platform.mapper.BaseMapper;
import com.onezol.platform.model.entity.BaseEntity;
import com.onezol.platform.service.BaseService;
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
        int affectedRows = this.baseMapper.opsForDelete("delete from " + tableName + " where id = " + id);
        return affectedRows > 0;
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
        String idsStr = handleIdList(ids);
        int affectedRows = this.baseMapper.opsForDelete("delete from " + tableName + " where id in (" + idsStr + ")");
        return affectedRows > 0;
    }

    /**
     * 忽视逻辑删除查询
     *
     * @param id id
     */
    @Override
    public T selectIgnoreLogicDelete(Serializable id) {
        String tableName = SqlHelper.table(this.currentModelClass()).getTableName();
        // 如果id是字符串，需要加上单引号
        if (id instanceof String) {
            id = "'" + id + "'";
        }
        return this.baseMapper.opsForSelect("select * from " + tableName + " where id = " + id);
    }

    /**
     * 忽视逻辑删除批量查询
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
        String idsStr = handleIdList(ids);
        return this.baseMapper.opsForSelectList("select * from " + tableName + " where id in (" + idsStr + ")");
    }

    /**
     * 处理id列表
     *
     * @param ids id列表
     * @return 处理后的id列表
     */
    private String handleIdList(Serializable[] ids) {
        // 如果id是字符串，需要加上单引号
        if (ids[0] instanceof String) {
            for (int i = 0; i < ids.length; i++) {
                ids[i] = "'" + ids[i] + "'";
            }
        }
        String idsStr = String.join(",", Arrays.toString(ids));
        idsStr = idsStr.substring(1, idsStr.length() - 1);
        return idsStr;
    }
}
