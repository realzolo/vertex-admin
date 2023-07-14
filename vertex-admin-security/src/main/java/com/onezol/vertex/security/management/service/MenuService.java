package com.onezol.vertex.security.management.service;

import com.onezol.vertex.core.common.service.GenericService;
import com.onezol.vertex.security.management.model.dto.Menu;
import com.onezol.vertex.security.management.model.entity.MenuEntity;

import java.util.List;
import java.util.Set;

public interface MenuService extends GenericService<MenuEntity> {
    /**
     * 根据用户id获取权限列表
     *
     * @param userId 用户id
     * @return permissions
     */
    Set<String> getPermsByUserId(Long userId);

    /**
     * 查询用户菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> getMenuListByUserId(Long userId);

    /**
     * 根据角色id获取菜单列表
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    List<Menu> getMenuListByRoleId(Long roleId);
}
