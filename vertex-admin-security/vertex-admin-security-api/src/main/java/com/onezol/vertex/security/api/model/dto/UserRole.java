package com.onezol.vertex.security.api.model.dto;

import com.onezol.vertex.common.model.dto.BaseDTO;

public class UserRole extends BaseDTO {
    private Long userId;
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
