package com.onezol.platform.controller;

import com.onezol.platform.annotation.ControllerService;
import com.onezol.platform.model.dto.RolePermission;
import com.onezol.platform.model.entity.RolePermissionEntity;
import com.onezol.platform.model.param.RolePermissionParam;
import com.onezol.platform.service.RolePermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role-permission")
@ControllerService(service = RolePermissionService.class, retClass = RolePermission.class)
public class RolePermissionController extends GenericController<RolePermissionEntity, RolePermissionParam> {
}
