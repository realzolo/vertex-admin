package com.onezol.platform.model.param;

public class DictKeyParam extends BaseParam {
    /**
     * 字典名称，如：性别
     */
    private String name;
    /**
     * 字典键，如：GENDER
     */
    private String key;
    /**
     * 备注
     */
    private String remark;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
