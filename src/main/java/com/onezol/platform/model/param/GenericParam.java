package com.onezol.platform.model.param;

import com.onezol.platform.annotation.Validator;

import java.util.Map;

public class GenericParam {
    // 当前页
    @Validator(minValue = 1)
    private Integer page;

    // 每页大小
    @Validator(minValue = 1, maxValue = 100)
    private Integer pageSize;

    // 排序字段与方式
    private String orderBy;

    // 查询字段
    private String[] fields;

    // 条件表达式
    private Map<String, Map<String, Object>> condition;

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
}
