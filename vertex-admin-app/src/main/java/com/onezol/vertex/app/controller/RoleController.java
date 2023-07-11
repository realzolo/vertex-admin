package com.onezol.vertex.app.controller;

import com.onezol.vertex.common.constant.enums.HttpStatus;
import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.core.annotation.ControllerService;
import com.onezol.vertex.core.model.pojo.SelectOption;
import com.onezol.vertex.security.model.dto.Role;
import com.onezol.vertex.security.model.entity.RoleEntity;
import com.onezol.vertex.security.model.param.RoleParam;
import com.onezol.vertex.security.service.RoleService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/select-options")
    public List<SelectOption> selectOptions() {
        return roleService.getRoleOptions();
    }
}