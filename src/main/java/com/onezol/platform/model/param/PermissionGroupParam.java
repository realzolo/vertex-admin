package com.onezol.platform.model.param;

import com.onezol.platform.annotation.Validator;

public class PermissionGroupParam {
    @Validator(required = true)
    private String name;
    @Validator(required = true)
    private String key;
    private String remark;
    private Boolean autoGeneratePermission;

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

    public Boolean getAutoGeneratePermission() {
        return autoGeneratePermission;
    }

    public void setAutoGeneratePermission(Boolean autoGeneratePermission) {
        this.autoGeneratePermission = autoGeneratePermission;
    }
}
