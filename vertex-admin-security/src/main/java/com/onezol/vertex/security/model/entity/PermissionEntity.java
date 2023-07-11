package com.onezol.vertex.security.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.core.common.model.entity.BaseEntity;

@TableName("pf_permission")
public class PermissionEntity extends BaseEntity {
    private Long groupId;
    private String name;
    @TableField("`key`")
    private String key;
    private String remark;

    public PermissionEntity() {
    }

    public PermissionEntity(Long groupId, String name, String key, String remark) {
        this.groupId = groupId;
        this.name = name;
        this.key = key;
        this.remark = remark;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}