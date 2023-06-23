package com.onezol.platform.service;

import com.onezol.platform.model.dto.DictValue;
import com.onezol.platform.model.entity.DictValueEntity;

public interface DictValueService extends BaseService<DictValueEntity> {
    /**
     * 保存字典值
     *
     * @param dictValue 字典值
     */
    void save(DictValue dictValue);
}
