package com.onezol.vertex.security.biz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.onezol.vertex.common.annotation.RestResponse;
import com.onezol.vertex.common.controller.BaseController;
import com.onezol.vertex.common.util.ObjectConverter;
import com.onezol.vertex.security.api.annotation.RestrictAccess;
import com.onezol.vertex.security.api.context.UserContext;
import com.onezol.vertex.security.api.context.UserContextHolder;
import com.onezol.vertex.security.api.model.dto.Menu;
import com.onezol.vertex.security.api.model.dto.User;
import com.onezol.vertex.security.api.model.entity.MenuEntity;
import com.onezol.vertex.security.api.model.payload.MenuPayload;
import com.onezol.vertex.security.api.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "菜单管理")
@RestrictAccess
@RestResponse
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController<MenuEntity> {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Operation(summary = "获取菜单", description = "根据ID获取菜单")
    @GetMapping
    public Menu getById(@RequestParam(value = "id") Long id) {
        MenuEntity menuEntity = menuService.getById(id);
        return ObjectConverter.convert(menuEntity, Menu.class);
    }

    @Operation(summary = "获取用户路由表", description = "获取用户菜单列表")
    @GetMapping("/routes")
    public List<Menu> getUserRoutes() {
        UserContext context = UserContextHolder.getContext();
        User user = context.getUserDetails();
        return menuService.getUserMenus(user.getId());
    }

    @Operation(summary = "获取下级菜单", description = "根据父级ID获取下级菜单列表")
    @GetMapping("/sublist")
    public Map<String, Object> getSublist(
            @RequestParam(value = "pageNo") Long pageNo,
            @RequestParam(value = "pageSize") Long pageSize,
            @RequestParam(value = "parentId", required = false) Long parentId
    ) {
        IPage<MenuEntity> page = getPage(pageNo, pageSize);

        return menuService.getSublist(page, parentId);
    }

    @Operation(summary = "获取菜单列表")
    @GetMapping("/menu-list")
    public Collection<Menu> getMenuList() {
        List<MenuEntity> menus = menuService.list();
        return ObjectConverter.convert(menus, Menu.class);
    }

    @Operation(summary = "获取菜单树")
    @GetMapping("/menu-tree")
    public List<Menu> getMenuTree() {
        return menuService.getMenuTree();
    }

    @Operation(summary = "菜单创建/更新", description = "菜单创建/更新")
    @PostMapping("/save")
    protected boolean save(@RequestBody MenuPayload payload) {
        return menuService.saveOrUpdate(payload);
    }

    @Operation(summary = "根据角色获取菜单列表", description = "根据角色ID获取菜单列表")
    @PostMapping("/role-menus")
    public List<Menu> getRoleMenus(@RequestParam("roleId") Long roleId) {
        if (Objects.isNull(roleId) || roleId <= 0L) {
            return Collections.emptyList();
        }
        return menuService.getRoleMenus(roleId);
    }

    @Operation(summary = "删除菜单", description = "根据ID删除菜单")
    @DeleteMapping
    public boolean delete(@RequestBody Long[] ids) {
        long[] idList = Arrays.stream(ids).distinct().mapToLong(Long::longValue).toArray();
        return menuService.deleteMenus(idList);
    }
}
