package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.platform.mapper.DictValueMapper;
import com.onezol.platform.model.dto.DictOption;
import com.onezol.platform.model.dto.DictValue;
import com.onezol.platform.model.entity.DictEntryEntity;
import com.onezol.platform.model.entity.DictValueEntity;
import com.onezol.platform.service.DictEntryService;
import com.onezol.platform.service.DictValueService;
import com.onezol.platform.util.ConvertUtils;
import com.onezol.platform.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.onezol.platform.constant.Constant.RK_DICTIONARY;

@Service
public class DictValueServiceImpl extends GenericServiceImpl<DictValueMapper, DictValueEntity> implements DictValueService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private DictEntryService dictEntryService;

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
            return ConvertUtils.convertMapToObject((Map) dictValue, DictValue.class);
        }
        DictValueEntity entity = this.getOne(Wrappers.<DictValueEntity>lambdaQuery().eq(DictValueEntity::getDictKey, key));
        return ConvertUtils.convertTo(entity, DictValue.class);
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
     * 根据字典键获取字典选项
     *
     * @param key 字典键
     * @return 字典选项
     */
    @Override
    public DictOption[] getOptionsByKey(String key) {
        DictValue dictValue = this.getByKey(key);
        if (dictValue == null) {
            return new DictOption[0];
        }
        DictEntryEntity dictEntryEntity = dictEntryService.getOne(
                Wrappers.<DictEntryEntity>lambdaQuery()
                        .select(DictEntryEntity::getId)
                        .eq(DictEntryEntity::getEntryKey, key)
        );
        if (dictEntryEntity == null) {
            return new DictOption[0];
        }
        List<DictValueEntity> entities = this.list(
                Wrappers.<DictValueEntity>lambdaQuery()
                        .eq(DictValueEntity::getEntryId, dictEntryEntity.getId())
        );
        DictOption[] options = new DictOption[entities.size()];
        for (int i = 0; i < entities.size(); i++) {
            options[i] = new DictOption();
            options[i].setKey(entities.get(i).getDictKey());
            options[i].setValue(entities.get(i).getValue());
            options[i].setCode(entities.get(i).getCode());
        }
        return options;
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
