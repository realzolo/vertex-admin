package com.onezol.platform.service;

import com.onezol.platform.model.dto.Permission;
import com.onezol.platform.model.entity.PermissionEntity;

import java.util.Set;

public interface PermissionService extends BaseService<PermissionEntity> {
    /**
     * 根据角色id获取权限列表
     *
     * @param roleId 角色id
     * @return permissions
     */
    Set<Permission> getByRoleId(Long roleId);

    /**
     * 根据角色id获取权限key列表
     *
     * @param roleId 角色id
     * @return permission keys
     */
    Set<String> getKeysByRoleId(Long roleId);

    /**
     * 根据用户id获取权限列表
     *
     * @param userId 用户id
     * @return permissions
     */
    Set<Permission> getByUserId(Long userId);

    /**
     * 根据用户id获取权限key列表
     *
     * @param userId 用户id
     * @return permission keys
     */
    Set<String> getKeysByUserId(Long userId);
}
