package com.onezol.platform.controller;

import com.onezol.platform.annotation.Validated;
import com.onezol.platform.model.dto.PermissionGroup;
import com.onezol.platform.model.param.PermissionGroupParam;
import com.onezol.platform.service.PermissionGroupService;
import com.onezol.platform.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/permission-group")
public class PermissionGroupController {
    @Autowired
    private PermissionGroupService permissionGroupService;

    @PostMapping("/create")
    public Object createPermission(@RequestBody @Validated PermissionGroupParam param) {
        boolean autoGeneratePermission = !Objects.isNull(param.getAutoGeneratePermission()) && param.getAutoGeneratePermission();
        PermissionGroup permissionGroup = ConvertUtils.convertTo(param, PermissionGroup.class);
        return permissionGroupService.createPermissionGroup(permissionGroup, autoGeneratePermission);
    }
}
