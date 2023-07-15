package com.onezol.vertex.core.security.management.model.dto;

import com.onezol.vertex.common.model.dto.BaseDTO;

public class Role extends BaseDTO {
    private String name;
    private String key;
    private String remark;

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
