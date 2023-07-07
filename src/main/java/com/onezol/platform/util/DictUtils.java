package com.onezol.platform.util;

import com.onezol.platform.model.dto.DictValue;
import com.onezol.platform.service.DictValueService;
import org.springframework.util.Assert;

public class DictUtils {
    private final static DictValueService dictValueService = SpringUtils.getBean(DictValueService.class);

    /**
     * 根据字典项与字典code获取字典值
     *
     * @param entryKey 字典项键
     * @param code     字典值编码
     * @return 字典值
     */
    public static String getDictValue(String entryKey, int code) {
        DictValue dictValue = dictValueService.getByCode(entryKey, code);
        Assert.notNull(dictValue, String.format("无法获取字典值, 请检查字典项: [%s] 或字典值编码: [%s] 是否存在", entryKey, code));
        return dictValue.getValue();
    }

    /**
     * 根据字典项与字典值获取code
     *
     * @param entryKey 字典项键
     * @param value    字典值
     * @return 字典值编码
     */
    public static int getDictCode(String entryKey, String value) {
        DictValue dictValue = dictValueService.getByValue(entryKey, value);
        Assert.notNull(dictValue, String.format("无法获取字典值, 请检查字典项: [%s] 或字典值: [%s] 是否存在", entryKey, value));
        return dictValue.getCode();
    }
}
