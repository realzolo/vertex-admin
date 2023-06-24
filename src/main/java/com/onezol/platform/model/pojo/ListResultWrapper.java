package com.onezol.platform.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ListResultWrapper<T> {
    private T[] items;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public ListResultWrapper() {
    }

    public ListResultWrapper(T[] items, Long total) {
        this.items = items;
        this.total = total;
    }

    public T[] getItems() {
        return items;
    }

    public void setItems(T[] items) {
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
