package com.onezol.vertex.app.controller;

import com.onezol.vertex.common.annotation.Loggable;
import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.base.controller.GenericController;
import com.onezol.vertex.core.module.file.FileService;
import com.onezol.vertex.core.module.file.model.dto.FileDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "文件管理")
@Loggable
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
    @Operation(summary = "查询文件", description = "查询文件详情")
    @PostMapping("/query")
    protected DTO queryById(@RequestBody GenericPayload payload) {
        return super.queryById(payload);
    }

    /**
     * 查询列表： /xxx/list
     */
    @Override
    @Operation(summary = "查询文件", description = "查询文件列表")
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericPayload payload) {
        return super.queryList(payload);
    }
}
