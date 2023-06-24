package com.onezol.platform.model.dto;

public class DictKey extends BaseDTO {
    /**
     * 字典名称，如：性别
     */
    private String name;
    /**
     * 字典键，如：GENDER
     */
    private String key;
    /**
     * 字典描述
     */
    private String description;

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
}
