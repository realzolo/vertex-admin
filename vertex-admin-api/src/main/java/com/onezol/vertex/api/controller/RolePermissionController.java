package com.onezol.vertex.api.controller;

import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.core.model.dto.RolePermission;
import com.onezol.vertex.core.model.entity.RolePermissionEntity;
import com.onezol.vertex.core.model.param.RolePermissionParam;
import com.onezol.vertex.core.service.RolePermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role-permission")
@ControllerService(service = RolePermissionService.class, retClass = RolePermission.class)
public class RolePermissionController extends GenericController<RolePermissionEntity, RolePermissionParam> {
}
