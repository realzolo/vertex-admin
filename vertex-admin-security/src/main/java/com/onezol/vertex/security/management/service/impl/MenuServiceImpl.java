package com.onezol.vertex.security.management.service.impl;

import com.onezol.vertex.core.common.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.util.ModelUtils;
import com.onezol.vertex.security.management.mapper.MenuMapper;
import com.onezol.vertex.security.management.model.dto.Menu;
import com.onezol.vertex.security.management.model.entity.MenuEntity;
import com.onezol.vertex.security.management.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class MenuServiceImpl extends GenericServiceImpl<MenuMapper, MenuEntity> implements MenuService {

    private final MenuMapper menuMapper;

    public MenuServiceImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    /**
     * 根据用户id获取权限列表
     *
     * @param userId 用户id
     * @return permissions
     */
    @Override
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
    @Override
    public List<Menu> getMenuListByUserId(Long userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            return Collections.emptyList();
        }
        List<MenuEntity> menuEntities = menuMapper.selectMenuListByUserId(userId);
        return ModelUtils.convert(menuEntities, Menu.class);
    }

    /**
     * 根据角色id获取菜单列表
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    @Override
    public List<Menu> getMenuListByRoleId(Long roleId) {
        if (Objects.isNull(roleId) || roleId <= 0) {
            return Collections.emptyList();
        }
        List<MenuEntity> menuEntities = menuMapper.selectMenuListByRoleId(roleId);
        return ModelUtils.convert(menuEntities, Menu.class);
    }
}
