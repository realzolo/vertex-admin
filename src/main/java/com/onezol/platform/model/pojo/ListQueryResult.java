package com.onezol.platform.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Zolo(Lixm)
 * @date 2022/12/4 18:06
 * @description ...
 */
public class ListQueryResult<T> {
    private T[] items;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public ListQueryResult() {
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
