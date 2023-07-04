package com.onezol.platform.model.param;

import com.onezol.platform.annotation.Validator;

public class DeleteParam {
    @Validator(required = true)
    private Long[] ids;
    private Boolean physicalDelete;

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    public Boolean getPhysicalDelete() {
        return physicalDelete;
    }

    public void setPhysicalDelete(Boolean physicalDelete) {
        this.physicalDelete = physicalDelete;
    }
}
