package com.onezol.platform.controller;

import com.onezol.platform.annotation.Validated;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.model.dto.DictValue;
import com.onezol.platform.model.entity.DictKeyEntity;
import com.onezol.platform.model.param.DictKeyParam;
import com.onezol.platform.model.param.DictValueParam;
import com.onezol.platform.service.DictKeyService;
import com.onezol.platform.service.DictValueService;
import com.onezol.platform.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict")
public class DictController {
    @Autowired
    private DictKeyService dictKeyService;
    @Autowired
    private DictValueService dictValueService;

    @PostMapping("/create-key")
    public void createKey(@RequestBody @Validated DictKeyParam param) {
        DictKeyEntity dictKeyEntity = ConvertUtils.convertTo(param, DictKeyEntity.class);
        boolean ok = dictKeyService.save(dictKeyEntity);
        if (!ok) {
            throw new BusinessException("创建字典键失败");
        }
    }

    @PostMapping("/create-value")
    public void createValue(@RequestBody @Validated DictValueParam param) {
        Long keyId = param.getKeyId();
        DictKeyEntity dictKeyEntity = dictKeyService.getById(keyId);
        if (dictKeyEntity == null) {
            throw new BusinessException("字典键不存在");
        }
        DictValue dictValue = ConvertUtils.convertTo(param, DictValue.class);
        dictValueService.save(dictValue);
    }
}
