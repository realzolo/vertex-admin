package com.onezol.vertex.core.model.param;

import com.onezol.vertex.common.model.BaseParam;

public class FileUploadParam extends BaseParam {
    private String type;
    private String subtype;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
}