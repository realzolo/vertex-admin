package com.onezol.vertex.app.controller;

import com.onezol.vertex.common.annotation.Anonymous;
import com.onezol.vertex.common.annotation.Loggable;
import com.onezol.vertex.common.constant.RedisKey;
import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.base.controller.GenericController;
import com.onezol.vertex.core.cache.RedisCache;
import com.onezol.vertex.core.module.dictionary.model.Dictionary;
import com.onezol.vertex.core.module.dictionary.model.DictionaryPayload;
import com.onezol.vertex.core.module.dictionary.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "字典管理")
@Loggable
@RestController
@RequestMapping("/dictionary")
public class DictionaryController extends GenericController<DictionaryService> {
    private final RedisCache redisCache;
    private final DictionaryService dictionaryService;

    public DictionaryController(RedisCache redisCache, DictionaryService dictionaryService) {
        this.redisCache = redisCache;
        this.dictionaryService = dictionaryService;
    }

    @Anonymous
    @GetMapping
    public Object getDictionary() {
        return redisCache.getCacheMap(RedisKey.DICTIONARY);
    }

    @Override
    @Operation(summary = "查询字典", description = "根据字典ID查询字典")
    @PostMapping("/query")
    protected DTO queryById(@RequestBody GenericPayload payload) {
        return super.queryById(payload);
    }

    @Override
    @Operation(summary = "查询字典列表", description = "根据条件查询字典列表")
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericPayload payload) {
        return super.queryList(payload);
    }

    @Operation(summary = "保存字典", description = "创建/更新字典")
    @PostMapping("/save")
    protected boolean save(@RequestBody DictionaryPayload payload) {
        return dictionaryService.saveOrUpdate(payload);
    }

    @Override
    @Operation(summary = "删除字典", description = "根据字典ID删除字典")
    @PostMapping("/delete")
    protected boolean delete(@RequestBody GenericPayload payload) {
        Long[] ids = payload.getIds();
        return dictionaryService.delete(ids);
    }

    @Override
    protected Class<? extends DTO> getDtoClass() {
        return Dictionary.class;
    }
}
