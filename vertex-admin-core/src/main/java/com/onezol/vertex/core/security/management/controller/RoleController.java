package com.onezol.vertex.core.security.management.controller;

import com.onezol.vertex.common.constant.enums.HttpStatus;
import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.common.model.record.SelectOption;
import com.onezol.vertex.core.base.controller.GenericController;
import com.onezol.vertex.core.security.management.model.dto.Role;
import com.onezol.vertex.core.security.management.model.payload.RoleMenuPayload;
import com.onezol.vertex.core.security.management.service.RoleService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController extends GenericController<RoleService> {
    @Autowired
    private RoleService roleService;

    /**
     * 权限分配
     */
    @PostMapping("/assign-permission/{roleId}")
    public void assignPermission(@PathVariable("roleId") Long roleId, @RequestBody Map<String, Long[]> payload) {
        if (roleId == null || roleId <= 0) {
            throw new BusinessException(HttpStatus.PARAM_ERROR, "无效的角色ID");
        }
        Long[] permissionIds = payload.get("permissionIds");
        if (permissionIds == null || ArrayUtils.isEmpty(permissionIds)) {
            throw new BusinessException(HttpStatus.PARAM_ERROR, "权限不能为空");
        }
        roleService.assignPermission(roleId, permissionIds);
    }

    @GetMapping("/select-options")
    public List<SelectOption> selectOptions() {
        return roleService.getRoleOptions();
    }

    /**
     * 查询列表： /xxx/list
     *
     * @param payload 查询参数
     */
    @Override
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericPayload payload) {
        return super.queryList(payload);
    }

    @PostMapping("/save-role-menu")
    public void saveRoleMenu(@RequestBody RoleMenuPayload payload) {
        if (payload.getRoleId() == null || payload.getRoleId() <= 0 || payload.getMenuIds() == null) {
            throw new BusinessException(HttpStatus.PARAM_ERROR);
        }
        roleService.saveRoleMenu(payload.getRoleId(), payload.getMenuIds());
    }

    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return Role.class;
    }
}
