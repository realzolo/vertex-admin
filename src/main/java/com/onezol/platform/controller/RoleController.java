package com.onezol.platform.controller;

import com.onezol.platform.annotation.ControllerService;
import com.onezol.platform.model.dto.Role;
import com.onezol.platform.model.entity.RoleEntity;
import com.onezol.platform.model.param.RoleParam;
import com.onezol.platform.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@ControllerService(service = RoleService.class, retClass = Role.class)
public class RoleController extends GenericController<RoleEntity, RoleParam> {

}
