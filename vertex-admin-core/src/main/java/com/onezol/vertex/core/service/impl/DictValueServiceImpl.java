package com.onezol.vertex.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.core.mapper.DictValueMapper;
import com.onezol.vertex.core.model.dto.DictValue;
import com.onezol.vertex.core.model.entity.DictEntryEntity;
import com.onezol.vertex.core.model.entity.DictValueEntity;
import com.onezol.vertex.core.model.pojo.SelectOption;
import com.onezol.vertex.core.service.DictEntryService;
import com.onezol.vertex.core.service.DictValueService;
import com.onezol.vertex.core.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.onezol.vertex.common.constant.CommonConstant.RK_DICTIONARY;

@Service
public class DictValueServiceImpl extends GenericServiceImpl<DictValueMapper, DictValueEntity> implements DictValueService {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private DictEntryService dictEntryService;
    @Autowired
    private DictValueMapper dictValueMapper;

    /**
     * 根据字典键获取字典值
     *
     * @param key 字典键
     * @return 字典值
     */
    @Override
    public DictValue getByKey(String key) {
        if (key == null) {
            return null;
        }
        Object dictValue = redisTemplate.opsForHash().get(RK_DICTIONARY, key);
        if (dictValue != null) {
            return ModelUtils.toObject((Map) dictValue, DictValue.class);
        }
        DictValueEntity entity = this.getOne(Wrappers.<DictValueEntity>lambdaQuery().eq(DictValueEntity::getDictKey, key));
        return ModelUtils.convert(entity, DictValue.class);
    }

    /**
     * 根据字典项与字典code获取字典值
     *
     * @param entryKey 字典项键
     * @param code     字典值编码
     * @return 字典值
     */
    @Override
    public DictValue getByCode(String entryKey, int code) {
        List<DictValueEntity> dictValueList = getDictValueListByEntryKey(entryKey);
        DictValueEntity dictValue = dictValueList.stream()
                .filter(item -> item.getCode() == code)
                .findFirst()
                .orElse(null);
        return ModelUtils.convert(dictValue, DictValue.class);
    }

    /**
     * 根据字典项与字典值获取code
     *
     * @param entryKey 字典项键
     * @param value    字典值
     * @return 字典值编码
     */
    @Override
    public DictValue getByValue(String entryKey, String value) {
        List<DictValueEntity> dictValueList = getDictValueListByEntryKey(entryKey);
        DictValueEntity dictValue = dictValueList.stream()
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElse(null);
        return ModelUtils.convert(dictValue, DictValue.class);
    }

    /**
     * 获取字典列表
     *
     * @return 字典列表
     */
    @Override
    public Map<String, List<SelectOption>> getDictionary() {
        Map<String, List<SelectOption>> dictMap = new HashMap<>();
        List<Map<String, Object>> mapList = dictValueMapper.getDictionary();
        for (Map<String, Object> map : mapList) {
            String entryKey = (String) map.get("ENTRY_KEY");
            List<SelectOption> m = dictMap.getOrDefault(entryKey, new ArrayList<>());
            SelectOption option = new SelectOption() {{
                setLabel(map.get("VALUE").toString());
                setValue(Long.valueOf(map.get("CODE").toString()));
            }};
            m.add(option);
            dictMap.put(entryKey, m);
        }
        return dictMap;
    }

    /**
     * 根据字典项获取字典值列表
     *
     * @param entryKey 字典项键
     * @return 字典值列表
     */
    private List<DictValueEntity> getDictValueListByEntryKey(String entryKey) {
        if (entryKey == null) return null;
        entryKey = entryKey.toUpperCase().trim();
        DictEntryEntity dictEntry = dictEntryService.getOne(
                Wrappers.<DictEntryEntity>lambdaQuery()
                        .select(DictEntryEntity::getId)
                        .eq(DictEntryEntity::getEntryKey, entryKey)
        );
        if (dictEntry == null) {
            return Collections.emptyList();
        }

        return this.list(
                Wrappers.<DictValueEntity>lambdaQuery()
                        .eq(DictValueEntity::getEntryId, dictEntry.getId())
        );
    }
}
