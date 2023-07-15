package com.onezol.vertex.core.security.management.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.constant.enums.HttpStatus;
import com.onezol.vertex.common.exception.BusinessException;
import com.onezol.vertex.common.model.record.SelectOption;
import com.onezol.vertex.core.base.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.security.management.mapper.RoleMapper;
import com.onezol.vertex.core.security.management.model.dto.Role;
import com.onezol.vertex.core.security.management.model.entity.RoleEntity;
import com.onezol.vertex.core.security.management.model.entity.RoleMenuEntity;
import com.onezol.vertex.core.security.management.model.entity.UserRoleEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"Duplicates"})
public class RoleService extends GenericServiceImpl<RoleMapper, RoleEntity> {
    private final UserRoleService userRoleService;
    private final RoleMenuService roleMenuService;

    public RoleService(UserRoleService userRoleService, RoleMenuService roleMenuService) {
        this.userRoleService = userRoleService;
        this.roleMenuService = roleMenuService;
    }

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return roles
     */
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
    public Set<String> getKeysByUserId(Long userId) {
        return this.getByUserId(userId).stream().map(Role::getKey).collect(Collectors.toSet());
    }

    /**
     * 为角色分配权限
     *
     * @param roleId        角色id
     * @param permissionIds 权限id
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignPermission(Long roleId, Long[] permissionIds) {
//        if (roleId == null || roleId <= 0) {
//            throw new IllegalArgumentException("无效的角色ID");
//        }
//        if (permissionIds == null || permissionIds.length == 0) {
//            throw new IllegalArgumentException("权限不能为空");
//        }
//        // 删除角色原有的权限
//        boolean ok = rolePermissionService.delete(
//                Wrappers.<RolePermissionEntity>lambdaQuery()
//                        .eq(RolePermissionEntity::getRoleId, roleId)
//        );
//        if (!ok) {
//            throw new BusinessException("权限分配失败");
//        }
//        // 为角色分配权限
//        permissionIds = Arrays.stream(permissionIds).distinct().toArray(Long[]::new);
//        List<RolePermissionEntity> entities = new ArrayList<>(permissionIds.length);
//        for (Long permissionId : permissionIds) {
//            RolePermissionEntity entity = new RolePermissionEntity();
//            entity.setRoleId(roleId);
//            entity.setPermissionId(permissionId);
//            entities.add(entity);
//        }
//        rolePermissionService.saveBatch(entities);
    }

    /**
     * 获取角色选项
     *
     * @return options
     */
    public List<SelectOption> getRoleOptions() {
        List<RoleEntity> list = this.list(
                Wrappers.<RoleEntity>lambdaQuery()
                        .select(RoleEntity::getId, RoleEntity::getName, RoleEntity::getKey)
        );
        return list.stream().map(roleEntity -> {
            SelectOption option = new SelectOption();
            option.setLabel(roleEntity.getName());
            option.setValue(roleEntity.getId());
            option.setKey(roleEntity.getKey());
            return option;
        }).collect(Collectors.toList());
    }

    /**
     * 保存角色菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    public void saveRoleMenu(Long roleId, Long[] menuIds) {
        if (roleId == null || roleId <= 0 || menuIds == null) {
            throw new BusinessException(HttpStatus.PARAM_ERROR);
        }
        // 先删除所有已存在的角色权限
        boolean ok = roleMenuService.delete(
                Wrappers.<RoleMenuEntity>lambdaQuery()
                        .eq(RoleMenuEntity::getRoleId, roleId)
        );
        if (ok) {
            List<RoleMenuEntity> entities = new ArrayList<>(menuIds.length);
            // 保存角色权限
            for (Long menuId : menuIds) {
                RoleMenuEntity entity = new RoleMenuEntity();
                entity.setRoleId(roleId);
                entity.setMenuId(menuId);
                entities.add(entity);
            }
            roleMenuService.saveBatch(entities);
        }
    }
}
