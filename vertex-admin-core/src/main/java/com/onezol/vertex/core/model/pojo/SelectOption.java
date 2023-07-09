package com.onezol.vertex.core.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
public class SelectOption {
    private String label;
    private Long value;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String key;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
