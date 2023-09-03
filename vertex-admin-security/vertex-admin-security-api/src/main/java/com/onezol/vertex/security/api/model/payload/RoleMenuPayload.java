package com.onezol.vertex.security.api.model.payload;

import com.onezol.vertex.common.model.payload.BasePayload;

public class RoleMenuPayload extends BasePayload {
    private Long roleId;
    private Long[] menuIds;

    public RoleMenuPayload() {
    }

    public RoleMenuPayload(Long roleId, Long[] menuIds) {
        this.roleId = roleId;
        this.menuIds = menuIds;
    }

    public Long getRoleId() {
        return roleId;
    }

    public RoleMenuPayload setRoleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public Long[] getMenuIds() {
        return menuIds;
    }

    public RoleMenuPayload setMenuIds(Long[] menuIds) {
        this.menuIds = menuIds;
        return this;
    }
}
