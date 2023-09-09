package com.onezol.vertex.core.module.dictionary.controller;

import com.onezol.vertex.common.annotation.RestResponse;
import com.onezol.vertex.common.constant.RedisKey;
import com.onezol.vertex.common.model.record.SelectOption;
import com.onezol.vertex.core.common.cache.RedisCache;
import com.onezol.vertex.core.module.dictionary.service.DictionaryService;
import com.onezol.vertex.security.api.annotation.RestrictAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "数据字典")
@RestController
@RestResponse
@RestrictAccess
@RequestMapping("/dictionary")
public class DictionaryController {
    private final RedisCache redisCache;
    private final DictionaryService dictionaryService;

    public DictionaryController(RedisCache redisCache, DictionaryService dictionaryService) {
        this.redisCache = redisCache;
        this.dictionaryService = dictionaryService;
    }

    @GetMapping
    @Operation(summary = "获取字典列表")
    public Map<String, List<SelectOption>> getDictionaryMap() {
        return redisCache.getCacheMap(RedisKey.DICTIONARY);
    }
}
