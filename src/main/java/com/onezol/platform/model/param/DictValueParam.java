package com.onezol.platform.model.param;

public class DictValueParam extends BaseParam {
    /**
     * 字典键ID
     */
    private Long keyId;
    /**
     * 字典键，如：GENDER
     */
    private String key;
    /**
     * 字典代码，如：1
     */
    private Integer code;
    /**
     * 字典值，如：男
     */
    private String value;
    /**
     * 备注
     */
    private String remark;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}