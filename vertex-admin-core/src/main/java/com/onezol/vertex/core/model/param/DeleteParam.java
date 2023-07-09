package com.onezol.vertex.core.model.param;


public class DeleteParam {
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
