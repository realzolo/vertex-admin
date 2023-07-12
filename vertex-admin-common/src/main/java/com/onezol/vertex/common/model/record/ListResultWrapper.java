package com.onezol.vertex.common.model.record;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ListResultWrapper<T> {
    private List<T> items;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public ListResultWrapper() {
    }

    public ListResultWrapper(List<T> items, Long total) {
        this.items = items;
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
