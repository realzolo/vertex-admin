package com.onezol.vertex.app.controller;

import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.core.model.dto.DictValue;
import com.onezol.vertex.core.model.entity.DictValueEntity;
import com.onezol.vertex.core.model.param.DictValueParam;
import com.onezol.vertex.core.model.pojo.SelectOption;
import com.onezol.vertex.core.service.DictValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.onezol.vertex.common.constant.CommonConstant.RK_ENUM_OPTIONS;


@RestController
@RequestMapping({"/dict-value", "/dictionary"})
@ControllerService(service = DictValueService.class, retClass = DictValue.class)
public class DictValueController extends GenericController<DictValueEntity, DictValueParam> {
    @Autowired
    private DictValueService dictValueService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public Object get() {
        // 枚举
        Map<Object, Object> enums = redisTemplate.opsForHash().entries(RK_ENUM_OPTIONS);
        // 字典
        Map<String, List<SelectOption>> dictionary = dictValueService.getDictionary();

        enums.putAll(dictionary);

        return enums;
    }
}
