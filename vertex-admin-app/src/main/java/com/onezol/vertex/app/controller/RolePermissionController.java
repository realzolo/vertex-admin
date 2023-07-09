package com.onezol.vertex.app.controller;

import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.security.model.dto.RolePermission;
import com.onezol.vertex.security.model.entity.RolePermissionEntity;
import com.onezol.vertex.security.model.param.RolePermissionParam;
import com.onezol.vertex.security.service.RolePermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role-permission")
@ControllerService(service = RolePermissionService.class, retClass = RolePermission.class)
public class RolePermissionController extends GenericController<RolePermissionEntity, RolePermissionParam> {
}
