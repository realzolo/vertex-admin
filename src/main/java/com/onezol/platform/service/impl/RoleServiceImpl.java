package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.mapper.RoleMapper;
import com.onezol.platform.model.dto.Role;
import com.onezol.platform.model.entity.RoleEntity;
import com.onezol.platform.model.entity.RolePermissionEntity;
import com.onezol.platform.model.entity.UserRoleEntity;
import com.onezol.platform.service.RolePermissionService;
import com.onezol.platform.service.RoleService;
import com.onezol.platform.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"Duplicates", "SpringJavaAutowiredFieldsWarningInspection"})
public class RoleServiceImpl extends GenericServiceImpl<RoleMapper, RoleEntity> implements RoleService {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return roles
     */
    @Override
    public Set<Role> getByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId不可为空");
        }
        // 获取用户角色关联表中的角色id列表
        List<Long> roleIds = userRoleService.list(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .select(UserRoleEntity::getRoleId)
                        .eq(UserRoleEntity::getUserId, userId)
        ).stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());

        // 根据角色id列表获取角色列表
        if (roleIds.isEmpty()) {
            return new HashSet<>();
        }
        List<RoleEntity> roleEntities = this.listByIds(roleIds);
        return roleEntities.stream().map(roleEntity -> {
            Role role = new Role();
            role.setId(roleEntity.getId());
            role.setName(roleEntity.getName());
            role.setKey(roleEntity.getKey());
            role.setRemark(roleEntity.getRemark());
            return role;
        }).collect(Collectors.toSet());
    }

    /**
     * 根据用户id获取角色key列表
     *
     * @param userId 用户id
     * @return role keys
     */
    @Override
    public Set<String> getKeysByUserId(Long userId) {
        return this.getByUserId(userId).stream().map(Role::getKey).collect(Collectors.toSet());
    }

    /**
     * 为角色分配权限
     *
     * @param roleId        角色id
     * @param permissionIds 权限id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermission(Long roleId, Long[] permissionIds) {
        if (roleId == null || roleId <= 0) {
            throw new IllegalArgumentException("无效的角色ID");
        }
        if (permissionIds == null || permissionIds.length == 0) {
            throw new IllegalArgumentException("权限不能为空");
        }
        // 删除角色原有的权限
        boolean ok = rolePermissionService.delete(
                Wrappers.<RolePermissionEntity>lambdaQuery()
                        .eq(RolePermissionEntity::getRoleId, roleId)
        );
        if (!ok) {
            throw new BusinessException("权限分配失败");
        }
        // 为角色分配权限
        permissionIds = Arrays.stream(permissionIds).distinct().toArray(Long[]::new);
        List<RolePermissionEntity> entities = new ArrayList<>(permissionIds.length);
        for (Long permissionId : permissionIds) {
            RolePermissionEntity entity = new RolePermissionEntity();
            entity.setRoleId(roleId);
            entity.setPermissionId(permissionId);
            entities.add(entity);
        }
        rolePermissionService.saveBatch(entities);
    }
}
