package com.onezol.vertex.security.biz.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.model.record.PlainPage;
import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.security.api.mapper.RoleMapper;
import com.onezol.vertex.security.api.mapper.UserRoleMapper;
import com.onezol.vertex.security.api.model.dto.Role;
import com.onezol.vertex.security.api.model.entity.RoleEntity;
import com.onezol.vertex.security.api.model.entity.RoleMenuEntity;
import com.onezol.vertex.security.api.model.entity.UserRoleEntity;
import com.onezol.vertex.security.api.service.RoleMenuService;
import com.onezol.vertex.security.api.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, RoleEntity> implements RoleService {
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMenuService roleMenuService;

    public RoleServiceImpl(RoleMapper roleMapper, UserRoleMapper userRoleMapper, RoleMenuService roleMenuService) {
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.roleMenuService = roleMenuService;
    }

    /**
     * 获取用户角色
     *
     * @param userId 用户ID
     */
    @Override
    public List<Role> getUserRoles(long userId) {
        // 获取用户角色关联表中的角色id列表
        Set<Long> roleIds = userRoleMapper.selectList(
                Wrappers.<UserRoleEntity>lambdaQuery()
                        .select(UserRoleEntity::getRoleId)
                        .eq(UserRoleEntity::getUserId, userId)
        ).stream().map(UserRoleEntity::getRoleId).collect(Collectors.toSet());

        // 根据角色id列表获取角色列表
        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<RoleEntity> roleEntities = this.listByIds(roleIds);
        return roleEntities.stream().map(roleEntity -> {
            Role role = new Role();
            role.setId(roleEntity.getId());
            role.setName(roleEntity.getName());
            role.setKey(roleEntity.getKey());
            role.setRemark(roleEntity.getRemark());
            return role;
        }).collect(Collectors.toList());
    }

    /**
     * 根据用户id获取角色key列表
     *
     * @param userId 用户id
     * @return role keys
     */
    @Override
    public Set<String> getUserRoleKeys(long userId) {
        return this.getUserRoles(userId).stream().map(Role::getKey).collect(Collectors.toSet());
    }

    /**
     * 获取角色列表
     *
     * @param page 分页对象
     * @return 角色列表
     */
    @Override
    public PlainPage<Role> getRoleList(IPage<RoleEntity> page) {
        IPage<RoleEntity> pageResult = this.page(page);
        return PlainPage.from(pageResult, Role.class);
    }

    /**
     * 保存角色菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    @Override
    @Transactional
    public boolean saveRoleMenu(long roleId, long[] menuIds) {
        // 先删除所有已存在的角色权限
        Wrapper<RoleMenuEntity> wrapper = Wrappers.<RoleMenuEntity>lambdaQuery()
                .eq(RoleMenuEntity::getRoleId, roleId);
        boolean ok = roleMenuService.delete(wrapper);
        if (ok) {
            List<RoleMenuEntity> entities = new ArrayList<>(menuIds.length);
            // 保存角色权限
            for (Long menuId : menuIds) {
                RoleMenuEntity entity = new RoleMenuEntity();
                entity.setRoleId(roleId);
                entity.setMenuId(menuId);
                entities.add(entity);
            }
            return roleMenuService.saveBatch(entities);
        }
        return false;
    }
}
