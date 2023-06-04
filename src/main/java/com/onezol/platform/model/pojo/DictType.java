package com.onezol.platform.model.pojo;

public class DictType {
    private int code;
    private String value;

    public DictType() {
    }

    public DictType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
