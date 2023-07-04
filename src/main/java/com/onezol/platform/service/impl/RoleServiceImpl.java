package com.onezol.platform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.platform.mapper.RoleMapper;
import com.onezol.platform.model.dto.Role;
import com.onezol.platform.model.entity.RoleEntity;
import com.onezol.platform.model.entity.UserRoleEntity;
import com.onezol.platform.service.RoleService;
import com.onezol.platform.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"Duplicates", "SpringJavaAutowiredFieldsWarningInspection"})
public class RoleServiceImpl extends GenericServiceImpl<RoleMapper, RoleEntity> implements RoleService {
    @Autowired
    private UserRoleService userRoleService;

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
}
