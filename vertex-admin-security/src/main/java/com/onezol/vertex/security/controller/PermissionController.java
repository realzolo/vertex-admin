package com.onezol.vertex.security.controller;

import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.core.common.model.dto.DTO;
import com.onezol.vertex.security.model.dto.Permission;
import com.onezol.vertex.security.service.PermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permission")
public class PermissionController extends GenericController<PermissionService> {


    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return Permission.class;
    }
}
