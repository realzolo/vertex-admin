package com.onezol.platform.service;

import com.onezol.platform.model.entity.BaseEntity;
import com.onezol.platform.model.param.BaseParam;
import com.onezol.platform.model.param.GenericParam;
import com.onezol.platform.model.pojo.ListResultWrapper;

public interface GenericService<T extends BaseEntity> extends BaseService<T> {
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
     * @param param 查询参数
     */
    ListResultWrapper<T> queryList(GenericParam param);

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
    T save(BaseParam data);
}
