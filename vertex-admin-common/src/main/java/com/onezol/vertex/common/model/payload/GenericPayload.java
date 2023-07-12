package com.onezol.vertex.common.model.payload;


import java.util.Map;

public final class GenericPayload extends BasePayload {
    /**
     * 当前页码(查询操作)
     */
    private Integer page;

    /**
     * 每页显示条数(查询操作)
     */
    private Integer pageSize;

    /**
     * 排序字段(查询操作)
     */
    private String orderBy;

    /**
     * 查询字段(查询操作)
     */
    private String[] fields;

    /**
     * 查询条件(查询操作)
     */
    private Map<String, Map<String, Object>> condition;

    /**
     * 提交数据(保存/更新操作)
     */
    private Map<String, Object> data;
    /**
     * ID数组(删除操作)
     */
    private Long[] ids;

    /**
     * 是否物理删除(删除操作)
     */
    private Boolean physicalDelete;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public Map<String, Map<String, Object>> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Map<String, Object>> condition) {
        this.condition = condition;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

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
