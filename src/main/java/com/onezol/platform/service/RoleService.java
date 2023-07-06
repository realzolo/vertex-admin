package com.onezol.platform.service;

import com.onezol.platform.model.dto.Role;
import com.onezol.platform.model.entity.RoleEntity;

import java.util.Set;

public interface RoleService extends GenericService<RoleEntity> {
    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return roles
     */
    Set<Role> getByUserId(Long userId);

    /**
     * 根据用户id获取角色key列表
     *
     * @param userId 用户id
     * @return role keys
     */
    Set<String> getKeysByUserId(Long userId);

    /**
     * 为角色分配权限
     *
     * @param roleId        角色id
     * @param permissionIds 权限id
     */
    void assignPermission(Long roleId, Long[] permissionIds);
}
