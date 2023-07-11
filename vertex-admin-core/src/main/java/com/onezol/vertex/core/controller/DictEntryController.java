package com.onezol.vertex.core.controller;

import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.core.common.model.dto.DTO;
import com.onezol.vertex.core.common.model.param.GenericParam;
import com.onezol.vertex.core.model.dto.DictEntry;
import com.onezol.vertex.core.service.DictEntryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict-key")
public class DictEntryController extends GenericController<DictEntryService> {
    /**
     * 查询: /xxx/query
     */
    @Override
    @PostMapping("/query")
    protected DTO queryById(@RequestBody GenericParam param) {
        return super.queryById(param);
    }

    /**
     * 查询列表： /xxx/list
     */
    @Override
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericParam param) {
        return super.queryList(param);
    }

    /**
     * 保存/更新： /xxx/save
     */
    @Override
    @PostMapping("/save")
    protected DTO save(@RequestBody GenericParam param) {
        return super.save(param);
    }

    /**
     * 删除： /xxx/delete
     */
    @Override
    @PostMapping("/delete")
    protected void delete(@RequestBody GenericParam param) {
        super.delete(param);
    }

    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return DictEntry.class;
    }
}
