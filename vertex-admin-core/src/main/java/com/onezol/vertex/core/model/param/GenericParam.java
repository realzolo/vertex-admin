package com.onezol.vertex.core.model.param;


import java.util.Map;

public class GenericParam {
    // 当前页
    private Integer page;

    // 每页大小
    private Integer pageSize;

    // 排序字段与方式
    private String orderBy;

    // 查询字段
    private String[] fields;

    // 条件表达式
    private Map<String, Map<String, Object>> condition;

    // 数据
    private Map<String, Object> data;

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
}
