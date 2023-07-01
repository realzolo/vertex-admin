package com.onezol.platform.service;

import com.onezol.platform.model.dto.DictOption;
import com.onezol.platform.model.dto.DictValue;
import com.onezol.platform.model.entity.DictValueEntity;

public interface DictValueService extends BaseService<DictValueEntity> {
    /**
     * 根据字典键获取字典值
     *
     * @param key 字典键
     * @return 字典值
     */
    DictValue getByKey(String key);

    /**
     * 根据字典键获取字典选项
     *
     * @param key 字典键
     * @return 字典选项
     */
    DictOption[] getOptionsByKey(String key);
}
