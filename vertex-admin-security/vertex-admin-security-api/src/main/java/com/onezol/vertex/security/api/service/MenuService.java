package com.onezol.vertex.security.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.onezol.vertex.common.service.BaseService;
import com.onezol.vertex.security.api.model.dto.Menu;
import com.onezol.vertex.security.api.model.entity.MenuEntity;
import com.onezol.vertex.security.api.model.payload.MenuPayload;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MenuService extends BaseService<MenuEntity> {
    /**
     * 获取用户菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> getUserMenus(long userId);

    /**
     * 获取用户权限列表
     *
     * @param userId 用户id
     * @return 权限keys
     */
    Set<String> getUserPermKeys(long userId);

    /**
     * 获取菜单列表
     *
     * @param parentId 父ID
     * @return 菜单列表
     */
    Map<String, Object> getSublist(IPage<MenuEntity> page, Long parentId);

    /**
     * 获取菜单树
     */
    List<Menu> getMenuTree();

    /**
     * 创建或更新菜单
     *
     * @param payload 菜单信息
     * @return 创建/保存状态
     */
    boolean saveOrUpdate(MenuPayload payload);

    /**
     * 获取角色菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<Menu> getRoleMenus(long roleId);

    /**
     * 删除菜单
     *
     * @param ids 菜单ID
     * @return 删除状态
     */
    boolean deleteMenus(long[] ids);

}
