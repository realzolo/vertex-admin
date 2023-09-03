package com.onezol.vertex.security.api.service;

import com.onezol.vertex.common.model.record.OptionType;
import com.onezol.vertex.common.service.BaseService;
import com.onezol.vertex.security.api.model.dto.Role;
import com.onezol.vertex.security.api.model.entity.RoleEntity;

import java.util.List;
import java.util.Set;

public interface RoleService extends BaseService<RoleEntity> {
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
    List<OptionType> getRoleOptions();

    /**
     * 保存角色菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    void saveRoleMenu(Long roleId, Long[] menuIds);
}
