package com.onezol.vertex.security.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.core.common.model.entity.BaseEntity;

@TableName("pf_user_role")
public class UserRoleEntity extends BaseEntity {
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
