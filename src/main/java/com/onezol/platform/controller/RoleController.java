package com.onezol.platform.controller;

import com.onezol.platform.annotation.ControllerService;
import com.onezol.platform.constant.enums.HttpStatus;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.model.dto.Role;
import com.onezol.platform.model.entity.RoleEntity;
import com.onezol.platform.model.param.RoleParam;
import com.onezol.platform.service.RoleService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/role")
@ControllerService(service = RoleService.class, retClass = Role.class)
public class RoleController extends GenericController<RoleEntity, RoleParam> {
    @Autowired
    private RoleService roleService;

    /**
     * 权限分配
     */
    @PostMapping("/assign-permission/{roleId}")
    public void assignPermission(@PathVariable("roleId") Long roleId, @RequestBody Map<String, Long[]> param) {
        if (roleId == null || roleId <= 0) {
            throw new BusinessException(HttpStatus.PARAM_ERROR, "无效的角色ID");
        }
        Long[] permissionIds = param.get("permissionIds");
        if (permissionIds == null || ArrayUtils.isEmpty(permissionIds)) {
            throw new BusinessException(HttpStatus.PARAM_ERROR, "权限不能为空");
        }
        roleService.assignPermission(roleId, permissionIds);
    }
}
