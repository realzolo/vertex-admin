package com.onezol.vertex.core.security.management.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.annotation.Anonymous;
import com.onezol.vertex.common.annotation.Loggable;
import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.core.base.controller.GenericController;
import com.onezol.vertex.core.security.management.model.dto.Menu;
import com.onezol.vertex.core.security.management.model.entity.MenuEntity;
import com.onezol.vertex.core.security.management.model.payload.MenuPayload;
import com.onezol.vertex.core.security.management.service.MenuService;
import com.onezol.vertex.core.util.ModelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "菜单管理")
@Loggable
@RestController
@RequestMapping("/menu")
public class MenuController extends GenericController<MenuService> {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    @Operation(summary = "查询菜单列表", description = "根据ID查询菜单")
    @PostMapping("/query")
    protected DTO queryById(@RequestBody GenericPayload payload) {
        return super.queryById(payload);
    }

    @Anonymous
    @Operation(summary = "查询用户菜单列表", description = "查询用户菜单列表")
    @GetMapping("/user-menu")
    public List<Menu> listByUser(@RequestParam Long userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return Collections.emptyList();
        }
        return menuService.getMenuListByUserId(userId);
    }

    @Operation(summary = "查询菜单列表", description = "查询菜单列表")
    @GetMapping("/type-menu")
    protected List<Menu> listByType(@RequestParam("types") String menuTypes) {
        String[] types = menuTypes.toUpperCase().split(",");
        List<MenuEntity> entities = menuService.list(
                Wrappers.<MenuEntity>lambdaQuery()
                        .in(MenuEntity::getMenuType, Arrays.asList(types))
        );
        return ModelUtils.convert(entities, Menu.class);
    }

    @Operation(summary = "查询角色菜单列表", description = "根据角色查询所拥有的菜单/权限")
    @GetMapping("/role-menu")
    public List<Menu> listByRole(@RequestParam Long roleId) {
        if (Objects.isNull(roleId) || roleId <= 0) {
            return Collections.emptyList();
        }
        return menuService.getMenuListByRoleId(roleId);
    }

    @Operation(summary = "查询子项菜单列表", description = "根据父级ID查询子项菜单列表")
    @GetMapping("/page")
    protected HashMap<String, Object> listByParent(@RequestParam Long parentId, @RequestParam Long page, @RequestParam Long pageSize) {
        if (Objects.isNull(parentId) || parentId <= 0) {
            parentId = 0L;
        }
        if (Objects.isNull(page) || page <= 0) {
            page = 1L;
        }
        if (Objects.isNull(pageSize) || pageSize <= 0) {
            pageSize = 10L;
        }
        return menuService.queryListByParentId(parentId, page, pageSize);
    }

    @Operation(summary = "菜单创建/更新", description = "菜单创建/更新")
    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('system:menu:create', 'system:menu:update')")
    protected void save(@RequestBody MenuPayload payload) {
        menuService.saveOrUpdate(payload);
    }

    @Override
    @Operation(summary = "菜单删除", description = "菜单删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    protected boolean delete(@RequestBody GenericPayload payload) {
        super.delete(payload);
        return false;
    }

    @Override
    protected Class<? extends DTO> getDtoClass() {
        return Menu.class;
    }
}
