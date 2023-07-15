package com.onezol.vertex.core.security.management.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.common.model.entity.BaseEntity;

@TableName("sys_role_menu")
public class RoleMenuEntity extends BaseEntity {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
