package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.platform.mapper.DictValueMapper;
import com.onezol.platform.model.dto.DictValue;
import com.onezol.platform.model.entity.DictValueEntity;
import com.onezol.platform.service.DictValueService;
import com.onezol.platform.util.ConvertUtils;
import org.springframework.stereotype.Service;

@Service
public class DictValueServiceImpl extends BaseServiceImpl<DictValueMapper, DictValueEntity> implements DictValueService {
    /**
     * 保存字典值
     *
     * @param dictValue 字典值
     */
    @Override
    public void save(DictValue dictValue) {
        long count = this.count(
                Wrappers.<DictValueEntity>lambdaQuery()
                        .eq(DictValueEntity::getKeyId, dictValue.getKeyId())
                        .eq(DictValueEntity::getCode, dictValue.getCode())
        );
        if (count > 0) {
            throw new RuntimeException("字典值已存在");
        }
        DictValueEntity dictValueEntity = ConvertUtils.convertTo(dictValue, DictValueEntity.class);
        boolean ok = this.save(dictValueEntity);
        if (!ok) {
            throw new RuntimeException("保存字典值失败");
        }
    }
//    /**
//     * 根据keyCode获取字典值
//     *
//     * @param keyCode 字典键
//     * @return 字典值列表
//     */
//    @Override
//    public List<DictValue> getDictValues(String keyCode) {
//        List<DictValueEntity> entities = this.list(
//                Wrappers.<DictValueEntity>lambdaQuery()
//                        .eq(DictValueEntity::getKeyCode, keyCode)
//        );
//        return ConvertUtils.convertTo(entities, DictValue.class);
//    }
}
