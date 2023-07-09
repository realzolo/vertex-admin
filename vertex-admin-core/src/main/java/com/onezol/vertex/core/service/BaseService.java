package com.onezol.vertex.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.onezol.vertex.common.model.BaseEntity;

import java.io.Serializable;

/**
 * 基础服务接口
 *
 * @param <T> BaseEntity的子类
 */
public interface BaseService<T extends BaseEntity> extends com.baomidou.mybatisplus.extension.service.IService<T> {
    /**
     * 物理删除
     *
     * @param id id
     */
    boolean deleteById(Serializable id);

    /**
     * 批量物理删除
     *
     * @param ids id列表
     */
    boolean deleteBatchByIds(Serializable[] ids);

    /**
     * 物理删除
     *
     * @param wrapper 条件构造器
     */
    boolean delete(Wrapper<T> wrapper);

    /**
     * 查询(忽视逻辑删除)
     *
     * @param id id
     * @return T
     */
    T selectIgnoreLogicDelete(Serializable id);

    /**
     * 查询(忽视逻辑删除)
     *
     * @param ids id列表
     * @return T[]
     */
    T[] selectIgnoreLogicDelete(Serializable[] ids);

    /**
     * 查询(忽视逻辑删除)
     *
     * @param wrapper 条件构造器
     * @return T
     */
    T[] selectIgnoreLogicDelete(Wrapper<T> wrapper);
}
