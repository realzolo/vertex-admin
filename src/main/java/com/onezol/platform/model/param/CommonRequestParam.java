package com.onezol.platform.model.param;

import com.onezol.platform.annotation.Validator;

import java.util.Map;

public class CommonRequestParam {
    // 服务名(模块名)
    @Validator(required = true)
    private String serviceName;

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

    // 数据
    private Map<String, Object> data;

    // 条件表达式
    private Map<String, Map<String, Object>> condition;

    // 唯一值属性
    private String[] unique;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

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

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Map<String, Object>> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Map<String, Object>> condition) {
        this.condition = condition;
    }

    public String[] getUnique() {
        return unique;
    }

    public void setUnique(String[] unique) {
        this.unique = unique;
    }
}
