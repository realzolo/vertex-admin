package com.onezol.vertex.security.biz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.onezol.vertex.common.annotation.RestResponse;
import com.onezol.vertex.common.controller.BaseController;
import com.onezol.vertex.common.model.record.PlainPage;
import com.onezol.vertex.security.api.annotation.RestrictAccess;
import com.onezol.vertex.security.api.model.dto.Role;
import com.onezol.vertex.security.api.model.entity.RoleEntity;
import com.onezol.vertex.security.api.model.payload.RoleMenuPayload;
import com.onezol.vertex.security.api.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@Tag(name = "菜单管理")
@RestrictAccess
@RestResponse
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<RoleEntity> {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "获取角色列表", description = "获取角色列表")
    @GetMapping("/list")
    public PlainPage<Role> list(
            @RequestParam("pageNo") Long pageNo,
            @RequestParam("pageSize") Long pageSize
    ) {
        IPage<RoleEntity> page = getPage(pageNo, pageSize);
        return roleService.getRoleList(page);
    }

    @Operation(summary = "创建角色菜单映射", description = "创建角色菜单映射")
    @PostMapping("/save-role-menu")
    public boolean saveRoleMenu(@RequestBody RoleMenuPayload payload) {
        long[] menuIds = Stream.of(payload.getMenuIds()).mapToLong(Long::longValue).toArray();
        return roleService.saveRoleMenu(payload.getRoleId(), menuIds);
    }

}
