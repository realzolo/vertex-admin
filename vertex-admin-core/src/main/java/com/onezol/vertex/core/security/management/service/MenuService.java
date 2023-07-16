package com.onezol.vertex.core.security.management.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.base.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.security.management.mapper.MenuMapper;
import com.onezol.vertex.core.security.management.model.dto.Menu;
import com.onezol.vertex.core.security.management.model.dto.Role;
import com.onezol.vertex.core.security.management.model.entity.MenuEntity;
import com.onezol.vertex.core.security.management.model.payload.MenuPayload;
import com.onezol.vertex.core.util.ModelUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService extends GenericServiceImpl<MenuMapper, MenuEntity> {

    private final MenuMapper menuMapper;
    private final RoleService roleService;

    public MenuService(MenuMapper menuMapper, RoleService roleService) {
        this.menuMapper = menuMapper;
        this.roleService = roleService;
    }

    /**
     * 根据用户id获取权限列表
     *
     * @param userId 用户id
     * @return permissions
     */
    public Set<String> getPermsByUserId(Long userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            throw new IllegalArgumentException("userId is null or less than 0");
        }
        return menuMapper.selectPermsByUserId(userId);
    }

    /**
     * 查询用户菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<Menu> getMenuListByUserId(Long userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return Collections.emptyList();
        }
        // 管理员需要查询所有菜单
        Set<Role> roleSet = roleService.getByUserId(userId);
        List<String> roles = roleSet.stream().map(Role::getKey).collect(Collectors.toList());
        boolean isAdmin = roles.contains("admin");

        List<MenuEntity> menuEntities = menuMapper.selectMenuListByUserId(userId, isAdmin);
        return ModelUtils.convert(menuEntities, Menu.class);
    }

    /**
     * 根据角色id获取菜单列表
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    public List<Menu> getMenuListByRoleId(Long roleId) {
        if (Objects.isNull(roleId) || roleId <= 0) {
            return Collections.emptyList();
        }
        List<MenuEntity> menuEntities = menuMapper.selectMenuListByRoleId(roleId);
        return ModelUtils.convert(menuEntities, Menu.class);
    }

    /**
     * 根据父级菜单获取子菜单列表
     *
     * @param parentId 父级ID
     * @param page     页码
     * @param pageSize 页大小
     * @return 子菜单列表
     */
    public HashMap<String, Object> queryListByParentId(Long parentId, Long page, Long pageSize) {
        Page<MenuEntity> objectPage = new Page<>(page, pageSize);

        // 根节点：查询所有根目录列表。
        if (parentId == 0) {
            Page<MenuEntity> pageResult = this.page(
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
        MenuEntity currentNode = this.getById(parentId);
        if (Objects.isNull(currentNode)) {
            return new HashMap<String, Object>() {{
                put("type", "M");
                put("items", Collections.emptyList());
                put("total", 0);
            }};
        }
        Page<MenuEntity> pageResult = this.page(
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
     * 保存或更新菜单
     *
     * @param payload 菜单信息
     */
    @Transactional
    public void saveOrUpdate(MenuPayload payload) {
        Long parentId = payload.getParentId();
        String menuType = payload.getMenuType();
        // 根节点：parentId = 0
        if (parentId == null || parentId == 0) {
            parentId = 0L;
        }

        MenuEntity entity = new MenuEntity();
        entity.setId(payload.getId());
        entity.setMenuName(payload.getMenuName());
        entity.setVisible(Objects.isNull(payload.getVisible()) || payload.getVisible());
        entity.setStatus(Objects.isNull(payload.getStatus()) || payload.getStatus());
        entity.setParentId(parentId);
        entity.setOrderNum(payload.getOrderNum());
        entity.setRemark(payload.getRemark());
        // 创建目录：menuType = M
        if ("M".equals(menuType)) {
            entity.setPath(payload.getPath());
            entity.setIcon(payload.getIcon());
            entity.setMenuType(menuType);
            entity.setIsCache(payload.getIsCache());
            entity.setIsFrame(payload.getIsFrame());
            entity.setQuery(payload.getQuery());
        }
        // 创建菜单：menuType = C
        if ("C".equals(menuType)) {
            entity.setPath(payload.getPath());
            entity.setComponent(payload.getComponent());
            entity.setIcon(payload.getIcon());
            entity.setMenuType(menuType);
            entity.setIsCache(payload.getIsCache());
            entity.setIsFrame(payload.getIsFrame());
            entity.setQuery(payload.getQuery());
        }
        // 创建按钮(权限)：menuType = F
        if ("F".equals(menuType)) {
            entity.setPerms(payload.getPerms());
            entity.setMenuType(menuType);
        }
        this.saveOrUpdate(entity);
    }

}
