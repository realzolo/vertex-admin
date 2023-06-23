package com.onezol.platform.model.param;

import com.onezol.platform.annotation.Validator;

public class DictValueParam {
    @Validator(required = true)
    private Long keyId;
    @Validator(required = true)
    private String key;
    @Validator(required = true)
    private Integer code;
    @Validator(required = true)
    private String value;
    private String description;

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
