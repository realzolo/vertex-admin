package com.onezol.vertex.security.management.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.common.annotation.Anonymous;
import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.common.controller.GenericController;
import com.onezol.vertex.core.util.ModelUtils;
import com.onezol.vertex.security.management.model.dto.Menu;
import com.onezol.vertex.security.management.model.entity.MenuEntity;
import com.onezol.vertex.security.management.model.payload.MenuPayload;
import com.onezol.vertex.security.management.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/menu")
public class MenuController extends GenericController<MenuService> {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 查询用户菜单列表
     *
     * @param userId 用户ID
     */
    @Anonymous
    @GetMapping("/list-by-user/{userId}")
    public List<Menu> queryUserMenuList(@PathVariable Long userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return Collections.emptyList();
        }
        return menuService.getMenuListByUserId(userId);
    }

    /**
     * 菜单管理：查询菜单列表
     */
    @GetMapping("/list/{menuTypes}")
    protected List<Menu> queryMenuList(@PathVariable String menuTypes) {
        String[] types = menuTypes.toUpperCase().split(",");
        List<MenuEntity> entities = menuService.list(
                Wrappers.<MenuEntity>lambdaQuery()
                        .in(MenuEntity::getMenuType, Arrays.asList(types))
        );
        return ModelUtils.convert(entities, Menu.class);
    }

    /**
     * 角色管理：根据角色查询所拥有的菜单/权限
     */
    @GetMapping("/list-by-role/{roleId}")
    public List<Menu> listBYRole(@PathVariable Long roleId) {
        if (Objects.isNull(roleId) || roleId <= 0) {
            return Collections.emptyList();
        }
        return menuService.getMenuListByRoleId(roleId);
    }

    /**
     * 菜单管理：根据父级ID查询子项菜单列表
     */
    @GetMapping("/list-by-page/{parentId}/{page}/{pageSize}")
    protected HashMap<String, Object> queryListByParentId(@PathVariable Long parentId, @PathVariable Long page, @PathVariable Long pageSize) {
        Page<MenuEntity> objectPage = new Page<>(page, pageSize);

        // 根节点：查询所有根目录列表。
        if (parentId == 0) {
            Page<MenuEntity> pageResult = menuService.page(
                    objectPage,
                    Wrappers.<MenuEntity>lambdaQuery()
                            .eq(MenuEntity::getParentId, 0)
                            .in(MenuEntity::getMenuType, "M", "C")
            );
            List<MenuEntity> records = pageResult.getRecords();
            return new HashMap<String, Object>() {{
                put("type", "M");
                put("items", ModelUtils.convert(records, Menu.class));
                put("total", pageResult.getTotal());
            }};
        }
        MenuEntity currentNode = menuService.getById(parentId);
        if (Objects.isNull(currentNode)) {
            return new HashMap<String, Object>() {{
                put("type", "M");
                put("items", Collections.emptyList());
                put("total", 0);
            }};
        }
        Page<MenuEntity> pageResult = menuService.page(
                objectPage,
                Wrappers.<MenuEntity>lambdaQuery()
                        .eq(MenuEntity::getParentId, parentId)
                        .in("M".equals(currentNode.getMenuType()), MenuEntity::getMenuType, "M", "C")
                        .in("C".equals(currentNode.getMenuType()), MenuEntity::getMenuType, "F")
        );
        List<MenuEntity> records = pageResult.getRecords();
        ListResultWrapper<MenuEntity> resultWrapper = new ListResultWrapper<>(records, pageResult.getTotal());
        return new HashMap<String, Object>() {{
            put("type", currentNode.getMenuType());
            put("items", ModelUtils.convert(resultWrapper.getItems(), Menu.class));
            put("total", resultWrapper.getTotal());
        }};
    }

    /**
     * 保存/更新： /xxx/save
     *
     * @param payload 保存参数
     */
    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('menu:create', 'menu:write')")
    protected void save(@RequestBody MenuPayload payload) {
        Long parentId = payload.getParentId();
        String menuType = payload.getMenuType();
        // 根节点：parentId = 0
        if (parentId == null || parentId == 0) {
            parentId = 0L;
        }

        MenuEntity entity = new MenuEntity();
        entity.setMenuName(payload.getMenuName());
        entity.setVisible(true);
        entity.setStatus(true);
        entity.setParentId(parentId);
        entity.setOrderNum(payload.getOrderNum());
        entity.setRemark(payload.getRemark());
        // 创建目录：menuType = M
        if ("M".equals(menuType)) {
            entity.setPath(payload.getPath());
            entity.setIcon(payload.getIcon());
            entity.setMenuType(menuType);
            entity.setCache(payload.getCache());
            entity.setFrame(payload.getFrame());
            entity.setQuery(payload.getQuery());
        }
        // 创建菜单：menuType = C
        if ("C".equals(menuType)) {
            entity.setPath(payload.getPath());
            entity.setComponent(payload.getComponent());
            entity.setIcon(payload.getIcon());
            entity.setMenuType(menuType);
            entity.setCache(payload.getCache());
            entity.setFrame(payload.getFrame());
            entity.setQuery(payload.getQuery());
        }
        // 创建按钮(权限)：menuType = F
        if ("F".equals(menuType)) {
            entity.setPerms(payload.getPerms());
            entity.setMenuType(menuType);
        }
        menuService.saveOrUpdate(entity);
    }

    /**
     * 删除： /xxx/delete
     *
     * @param payload 删除参数
     */
    @Override
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('menu:delete')")
    protected void delete(@RequestBody GenericPayload payload) {
        super.delete(payload);
    }

    /**
     * 获取DTO的Class(返回给前端的数据类型, 避免Entity直接暴露给前端)
     *
     * @return DTO Class
     */
    @Override
    protected Class<? extends DTO> getDtoClass() {
        return Menu.class;
    }
}
