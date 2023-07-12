package com.onezol.vertex.core.runner;


import com.onezol.vertex.core.module.dictionary.model.dto.DictValue;
import com.onezol.vertex.core.module.dictionary.model.entity.DictValueEntity;
import com.onezol.vertex.core.module.dictionary.service.DictValueService;
import com.onezol.vertex.core.util.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

import static com.onezol.vertex.common.constant.CommonConstant.RK_DICTIONARY;

@Component
public class DictionarySyncRunner implements ApplicationRunner {
    public static final Logger logger = LoggerFactory.getLogger(DictionarySyncRunner.class);

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private DictValueService dictValueService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 获取所有字典值
        List<DictValueEntity> values = dictValueService.list();
        List<DictValue> dictValues = ModelUtils.convert(values, DictValue.class);
        HashMap<String, DictValue> dictionaryMap = new HashMap<>();
        dictValues.forEach(dictValue -> {
            dictionaryMap.put(dictValue.getDictKey(), dictValue);
        });

        // 将字典数据保存到Redis缓存中
        redisTemplate.opsForHash().putAll(RK_DICTIONARY, dictionaryMap);
        logger.info("字典数据已同步到Redis缓存中，共 {} 项", dictionaryMap.size());
    }
}
