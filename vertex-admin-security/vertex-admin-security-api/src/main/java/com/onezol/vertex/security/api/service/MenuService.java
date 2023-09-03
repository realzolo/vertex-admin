package com.onezol.vertex.security.api.service;

import com.onezol.vertex.common.service.BaseService;
import com.onezol.vertex.security.api.model.dto.Menu;
import com.onezol.vertex.security.api.model.entity.MenuEntity;
import com.onezol.vertex.security.api.model.payload.MenuPayload;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MenuService extends BaseService<MenuEntity> {
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

    /**
     * 根据父级菜单获取子菜单列表
     *
     * @param parentId 父级ID
     * @param page     页码
     * @param pageSize 页大小
     * @return 子菜单列表
     */
    Map<String, Object> queryListByParentId(Long parentId, Long page, Long pageSize);

    /**
     * 保存或更新菜单
     *
     * @param payload 菜单信息
     */
    void saveOrUpdate(MenuPayload payload);
}
