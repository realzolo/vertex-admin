package com.onezol.vertex.core.controller;

import com.onezol.vertex.common.pojo.ListResultWrapper;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.core.common.model.dto.DTO;
import com.onezol.vertex.core.common.model.param.GenericParam;
import com.onezol.vertex.core.model.dto.FileDetail;
import com.onezol.vertex.core.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileController extends GenericController<FileService> {
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return FileDetail.class;
    }

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
}
