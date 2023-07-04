package com.onezol.platform.service;

import com.onezol.platform.model.entity.BaseEntity;
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
     * @param id 主键
     */
    void delete(Long id);
}
