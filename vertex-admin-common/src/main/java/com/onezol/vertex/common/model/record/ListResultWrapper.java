package com.onezol.vertex.common.model.record;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@Deprecated
public class ListResultWrapper<T> {
    private Collection<T> items;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public ListResultWrapper() {
    }

    public ListResultWrapper(Collection<T> items, Long total) {
        this.items = items;
        this.total = total;
    }

    public Collection<T> getItems() {
        return items;
    }

    public void setItems(Collection<T> items) {
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
