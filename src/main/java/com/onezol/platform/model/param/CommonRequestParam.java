package com.onezol.platform.model.param;

import com.onezol.platform.annotation.Validator;

import java.util.Map;

public class CommonRequestParam {
    // 服务名(模块名)
    @Validator(required = true)
    private String service;

    // 当前页
    private Integer pageSize;

    // 每页大小
    @Validator(maxValue = 100)
    private Integer page;

    // 排序字段与方式
    private String orderBy;

    // 查询条件
    private String condition;

    // 查询字段
    private String[] fields;

    // 数据
    private Map<String, Object> data;

    public CommonRequestParam() {
    }

    public CommonRequestParam(String service, Integer pageSize, Integer page, String orderBy, String condition, String[] fields, Map<String, Object> data) {
        this.service = service;
        this.pageSize = pageSize;
        this.page = page;
        this.orderBy = orderBy;
        this.condition = condition;
        this.fields = fields;
        this.data = data;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
}
