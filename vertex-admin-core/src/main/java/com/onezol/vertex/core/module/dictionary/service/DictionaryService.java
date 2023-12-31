package com.onezol.vertex.core.module.dictionary.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.constant.enums.HttpStatus;
import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.model.record.SelectOption;
import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.common.util.StringUtils;
import com.onezol.vertex.core.common.util.ModelUtils;
import com.onezol.vertex.core.module.dictionary.mapper.DictionaryMapper;
import com.onezol.vertex.core.module.dictionary.model.DictionaryEntity;
import com.onezol.vertex.core.module.dictionary.model.DictionaryPayload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DictionaryService extends BaseServiceImpl<DictionaryMapper, DictionaryEntity> {

    /**
     * 将字典转换为Map<String, SelectOption[]>
     */
    public Map<String, List<SelectOption>> getDictionaryMap() {
        Wrapper<DictionaryEntity> wrapper = Wrappers.<DictionaryEntity>lambdaQuery()
                .select(
                        DictionaryEntity::getId,
                        DictionaryEntity::getDictKey,
                        DictionaryEntity::getDictValue,
                        DictionaryEntity::getDictCode,
                        DictionaryEntity::getParentId
                );
        List<DictionaryEntity> entities = this.list(wrapper);
        Map<Long, DictionaryEntity> keyMap = new HashMap<>();
        Map<String, List<SelectOption>> map = new HashMap<>();
        entities.stream()
                .filter(entity -> Objects.isNull(entity.getParentId()) || entity.getParentId() == 0L)
                .forEach(entity -> {
                    keyMap.put(entity.getId(), entity);
                    map.put(entity.getDictKey(), new ArrayList<>());
                });

        entities.stream()
                .filter(entity -> Objects.nonNull(entity.getParentId()) && entity.getParentId() > 0L)
                .forEach(entity -> {
                    DictionaryEntity parent = keyMap.get(entity.getParentId());
                    if (Objects.nonNull(parent)) {
                        List<SelectOption> options = map.get(parent.getDictKey());
                        options.add(new SelectOption(entity.getDictValue(), entity.getDictCode()));
                    }
                });

        // 移除key为null的数据
        map.remove(null);
        return map;
    }

    /**
     * 字典删除
     */
    @Transactional
    public boolean delete(Long[] ids) {
        if (Objects.isNull(ids)) {
            throw new BusinessException(HttpStatus.PARAM_ERROR, "ids can not be null");
        }
        List<Long> idList = Arrays.stream(ids).collect(Collectors.toList());
        // 找出所有子节点
        List<Long> childIds = this.list(
                        Wrappers.<DictionaryEntity>lambdaQuery()
                                .select(DictionaryEntity::getId)
                                .in(DictionaryEntity::getParentId, idList)
                ).stream()
                .map(DictionaryEntity::getId)
                .collect(Collectors.toList());
        // 删除节点
        idList.addAll(childIds);
        return this.deleteBatchByIds(idList.toArray(new Long[0]));
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(DictionaryPayload payload) {
        if (Objects.isNull(payload)) {
            throw new BusinessException(HttpStatus.PARAM_ERROR, "payload can not be null");
        }
        long count = this.count(
                Wrappers.<DictionaryEntity>lambdaQuery()
                        .eq(DictionaryEntity::getDictKey, payload.getDictKey())
        );
        if (count > 0) {
            throw new BusinessException(HttpStatus.PARAM_ERROR, "dictKey already exists");
        }
        DictionaryEntity entity = ModelUtils.convert(payload, DictionaryEntity.class);
        return this.saveOrUpdate(entity);
    }

    /**
     * 字典翻英
     *
     * @param dictKey  字典key
     * @param dictCode 字典code
     * @return 字典值
     */
    public String translate(String dictKey, int dictCode) {
        Wrapper<DictionaryEntity> wrapper = Wrappers.<DictionaryEntity>lambdaQuery()
                .select(DictionaryEntity::getDictValue)
                .eq(DictionaryEntity::getDictKey, dictKey)
                .eq(DictionaryEntity::getDictCode, dictCode);
        DictionaryEntity entity = this.getOne(wrapper);

        return Optional.ofNullable(entity).map(DictionaryEntity::getDictValue).orElse(StringUtils.EMPTY);
    }
}
