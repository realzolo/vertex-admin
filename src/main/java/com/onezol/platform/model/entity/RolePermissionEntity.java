package com.onezol.platform.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("pf_role_permission")
public class RolePermissionEntity extends BaseEntity {
    private Long roleId;
    private Long permissionId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
}
