package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.platform.mapper.DictValueMapper;
import com.onezol.platform.model.dto.DictOption;
import com.onezol.platform.model.dto.DictValue;
import com.onezol.platform.model.entity.DictKeyEntity;
import com.onezol.platform.model.entity.DictValueEntity;
import com.onezol.platform.service.DictKeyService;
import com.onezol.platform.service.DictValueService;
import com.onezol.platform.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.onezol.platform.constant.Constant.RK_DICTIONARY;

@Service
public class DictValueServiceImpl extends BaseServiceImpl<DictValueMapper, DictValueEntity> implements DictValueService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private DictKeyService dictKeyService;

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
        DictValueEntity entity = this.getOne(Wrappers.<DictValueEntity>lambdaQuery().eq(DictValueEntity::getKey, key));
        return ConvertUtils.convertTo(entity, DictValue.class);
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
        DictKeyEntity dictKeyEntity = dictKeyService.getOne(
                Wrappers.<DictKeyEntity>lambdaQuery()
                        .select(DictKeyEntity::getId)
                        .eq(DictKeyEntity::getKey, key)
        );
        if (dictKeyEntity == null) {
            return new DictOption[0];
        }
        List<DictValueEntity> entities = this.list(
                Wrappers.<DictValueEntity>lambdaQuery()
                        .eq(DictValueEntity::getKeyId, dictKeyEntity.getId())
        );
        DictOption[] options = new DictOption[entities.size()];
        for (int i = 0; i < entities.size(); i++) {
            options[i] = new DictOption();
            options[i].setKey(entities.get(i).getKey());
            options[i].setValue(entities.get(i).getValue());
            options[i].setCode(entities.get(i).getCode());
        }
        return options;
    }
}
