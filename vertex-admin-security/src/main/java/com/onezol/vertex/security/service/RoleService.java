package com.onezol.vertex.security.service;

import com.onezol.vertex.core.model.pojo.SelectOption;
import com.onezol.vertex.core.service.GenericService;
import com.onezol.vertex.security.model.dto.Role;
import com.onezol.vertex.security.model.entity.RoleEntity;

import java.util.List;
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

    /**
     * 获取角色选项
     *
     * @return options
     */
    List<SelectOption> getRoleOptions();
}
