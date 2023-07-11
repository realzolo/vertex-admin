package com.onezol.vertex.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.core.common.service.impl.GenericServiceImpl;
import com.onezol.vertex.security.mapper.PermissionMapper;
import com.onezol.vertex.security.model.dto.Permission;
import com.onezol.vertex.security.model.entity.PermissionEntity;
import com.onezol.vertex.security.model.entity.RolePermissionEntity;
import com.onezol.vertex.security.model.entity.UserRoleEntity;
import com.onezol.vertex.security.service.PermissionService;
import com.onezol.vertex.security.service.RolePermissionService;
import com.onezol.vertex.security.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@SuppressWarnings({"Duplicates", "SpringJavaAutowiredFieldsWarningInspection"})
public class PermissionServiceImplImpl extends GenericServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 根据角色id获取权限列表
     *
     * @param roleId 角色id
     * @return permissions
     */
    @Override
    public Set<Permission> getByRoleId(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("roleId不可为空");
        }
        // 获取角色权限关联表中的权限id列表
        List<Long> permissionIds = rolePermissionService.list(
                Wrappers.<RolePermissionEntity>lambdaQuery()
                        .select(RolePermissionEntity::getPermissionId)
                        .eq(RolePermissionEntity::getRoleId, roleId)
        ).stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());

        // 根据权限id列表获取权限列表
        if (permissionIds.isEmpty()) {
            return new HashSet<>();
        }
        List<PermissionEntity> permissionEntities = this.listByIds(permissionIds);

        // 将权限列表转换为Permission对象列表
        return permissionEntities.stream().map(permissionEntity -> {
            Permission permission = new Permission();
            permission.setId(permissionEntity.getId());
            permission.setName(permissionEntity.getName());
            permission.setKey(permissionEntity.getKey());
            permission.setRemark(permissionEntity.getRemark());
            return permission;
        }).collect(Collectors.toSet());
    }

    /**
     * 根据角色id获取权限key列表
     *
     * @param roleId 角色id
     * @return permission keys
     */
    @Override
    public Set<String> getKeysByRoleId(Long roleId) {
        return this.getByRoleId(roleId).stream().map(Permission::getKey).collect(Collectors.toSet());
    }

    /**
     * 根据用户id获取权限列表
     *
     * @param userId 用户id
     * @return permissions
     */
    @Override
    public Set<Permission> getByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId不可为空");
        }

        // 获取用户角色关联表中的角色id列表
        List<Long> roleIds = userRoleService.list(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .select(UserRoleEntity::getRoleId)
                        .eq(UserRoleEntity::getUserId, userId)
        ).stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());

        // 根据角色id列表获取权限列表
        if (roleIds.isEmpty()) {
            return new HashSet<>();
        }
        return roleIds.stream().map(this::getByRoleId).flatMap(Set::stream).collect(Collectors.toSet());
    }

    /**
     * 根据用户id获取权限key列表
     *
     * @param userId 用户id
     * @return permission keys
     */
    @Override
    public Set<String> getKeysByUserId(Long userId) {
        return this.getByUserId(userId).stream().map(Permission::getKey).collect(Collectors.toSet());
    }
}
