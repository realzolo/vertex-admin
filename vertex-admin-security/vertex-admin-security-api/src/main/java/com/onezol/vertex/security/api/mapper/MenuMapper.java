package com.onezol.vertex.security.api.mapper;

import com.onezol.vertex.common.mapper.BaseMapper;
import com.onezol.vertex.security.api.model.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface MenuMapper extends BaseMapper<MenuEntity> {
    /**
     * 根据用户ID查询权限列表
     */
    Set<String> selectPermsByUserId(Long userId);

    /**
     * 查询用户菜单列表
     */
    List<MenuEntity> selectMenuListByUserId(Long userId, boolean isAdmin);

    /**
     * 根据角色id获取菜单列表
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    List<MenuEntity> selectMenuListByRoleId(Long roleId);
}
