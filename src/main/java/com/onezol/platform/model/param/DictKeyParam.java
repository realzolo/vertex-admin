package com.onezol.platform.model.param;

import com.onezol.platform.annotation.Validator;

public class DictKeyParam {

    @Validator(required = true)
    private String name;

    @Validator(required = true)
    private String key;

    private String description;

    private String parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
