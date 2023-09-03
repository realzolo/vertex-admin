package com.onezol.vertex.security.biz.service;

import com.onezol.vertex.common.model.record.OptionType;
import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.security.api.mapper.RoleMapper;
import com.onezol.vertex.security.api.model.dto.Role;
import com.onezol.vertex.security.api.model.entity.RoleEntity;
import com.onezol.vertex.security.api.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return roles
     */
    @Override
    public Set<Role> getByUserId(Long userId) {
        return null;
    }

    /**
     * 根据用户id获取角色key列表
     *
     * @param userId 用户id
     * @return role keys
     */
    @Override
    public Set<String> getKeysByUserId(Long userId) {
        return null;
    }

    /**
     * 为角色分配权限
     *
     * @param roleId        角色id
     * @param permissionIds 权限id
     */
    @Override
    public void assignPermission(Long roleId, Long[] permissionIds) {

    }

    /**
     * 获取角色选项
     *
     * @return options
     */
    @Override
    public List<OptionType> getRoleOptions() {
        return null;
    }

    /**
     * 保存角色菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    @Override
    public void saveRoleMenu(Long roleId, Long[] menuIds) {

    }
}
