package com.onezol.vertex.security.model.param;

import com.onezol.vertex.core.common.model.param.BaseParam;

public class PermissionGroupParam extends BaseParam {
    private String name;
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
