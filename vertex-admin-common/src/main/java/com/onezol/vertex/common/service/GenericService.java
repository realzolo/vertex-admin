package com.onezol.vertex.common.service;

import com.onezol.vertex.common.model.entity.Entity;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;

import java.util.Map;

@Deprecated
public interface GenericService<T extends Entity> extends BaseService<T> {
    /**
     * 根据id查询
     *
     * @param id 主键
     * @return 实体
     */
    T queryOne(Long id);

    /**
     * 条件查询
     *
     * @param payload 查询参数
     */
    ListResultWrapper<T> queryList(GenericPayload payload);

    /**
     * 删除
     *
     * @param id             ID
     * @param physicalDelete 是否物理删除
     */
    void delete(Long id, boolean physicalDelete);

    /**
     * 删除
     *
     * @param physicalDelete 是否物理删除
     * @param ids            ID列表
     */
    void delete(Long[] ids, boolean physicalDelete);

    /**
     * 保存/更新
     *
     * @param data 数据
     * @return 保存/更新结果
     */
    T save(Map<String, Object> data);
}
