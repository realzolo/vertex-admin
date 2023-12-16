package com.onezol.vertex.security.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.onezol.vertex.common.model.record.PlainPage;
import com.onezol.vertex.common.service.BaseService;
import com.onezol.vertex.security.api.model.dto.Role;
import com.onezol.vertex.security.api.model.entity.RoleEntity;

import java.util.List;
import java.util.Set;

public interface RoleService extends BaseService<RoleEntity> {
    /**
     * 获取用户角色
     *
     * @param userId 用户ID
     */
    List<Role> getUserRoles(long userId);

    /**
     * 根据用户id获取角色key列表
     *
     * @param userId 用户id
     * @return role keys
     */
    Set<String> getUserRoleKeys(long userId);

    /**
     * 获取角色列表
     *
     * @param page 分页对象
     * @return 角色列表
     */
    PlainPage<Role> getRoleList(IPage<RoleEntity> page);

    /**
     * 保存角色菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    boolean saveRoleMenu(long roleId, long[] menuIds);
}
