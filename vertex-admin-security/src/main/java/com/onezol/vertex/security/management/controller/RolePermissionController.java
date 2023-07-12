package com.onezol.vertex.security.management.controller;

import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.security.management.model.dto.RolePermission;
import com.onezol.vertex.security.management.service.RolePermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role-permission")
public class RolePermissionController extends GenericController<RolePermissionService> {
    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return RolePermission.class;
    }
}
