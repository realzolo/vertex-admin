package com.onezol.vertex.app.controller;

import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.core.module.dictionary.model.dto.DictValue;
import com.onezol.vertex.core.module.dictionary.service.DictValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping({"/dict-value", "/dictionary"})
public class DictValueController extends GenericController<DictValueService> {
    @Autowired
    private DictValueService dictValueService;

//    @GetMapping
//    public Object get() {
//        // 枚举
//        Map<Object, Object> enums = redisTemplate.opsForHash().entries(RK_ENUM_OPTIONS);
//        // 字典
//        Map<String, List<SelectOption>> dictionary = dictValueService.getDictionary();
//
//        enums.putAll(dictionary);
//
//        return enums;
//    }

    /**
     * 查询: /xxx/query
     */
    @Override
    @PostMapping("/query")
    protected DTO queryById(@RequestBody GenericPayload payload) {
        return super.queryById(payload);
    }

    /**
     * 查询列表： /xxx/list
     */
    @Override
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericPayload payload) {
        return super.queryList(payload);
    }

    /**
     * 保存/更新： /xxx/save
     *
     */
    @Override
    @PostMapping("/save")
    protected DTO save(@RequestBody GenericPayload payload) {
        return super.save(payload);
    }

    /**
     * 删除： /xxx/delete
     *
     */
    @Override
    @PostMapping("/delete")
    protected void delete(@RequestBody GenericPayload payload) {
        super.delete(payload);
    }

    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return DictValue.class;
    }
}
