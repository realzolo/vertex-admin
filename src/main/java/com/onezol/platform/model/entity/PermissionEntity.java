package com.onezol.platform.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("pf_permission")
public class PermissionEntity extends BaseEntity {
    private String name;
    private String key;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}