package com.onezol.platform.service;

import com.onezol.platform.model.dto.DictOption;
import com.onezol.platform.model.dto.DictValue;
import com.onezol.platform.model.entity.DictValueEntity;

import java.util.List;
import java.util.Map;

public interface DictValueService extends GenericService<DictValueEntity> {
    /**
     * 根据字典键获取字典值
     *
     * @param dictKey 字典键
     * @return 字典值
     */
    DictValue getByKey(String dictKey);

    /**
     * 根据字典项与字典code获取字典值
     *
     * @param entryKey 字典项键
     * @param code     字典值编码
     * @return 字典值
     */
    DictValue getByCode(String entryKey, int code);

    /**
     * 根据字典项与字典值获取code
     *
     * @param entryKey 字典项键
     * @param value    字典值
     * @return 字典值编码
     */
    DictValue getByValue(String entryKey, String value);

    /**
     * 获取字典列表
     *
     * @return 字典列表
     */
    Map<String, List<DictOption>> getDictionary();
}
