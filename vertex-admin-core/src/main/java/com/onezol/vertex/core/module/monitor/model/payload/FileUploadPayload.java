package com.onezol.vertex.core.module.monitor.model.payload;

import com.onezol.vertex.common.model.payload.BasePayload;

public class FileUploadPayload extends BasePayload {
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
