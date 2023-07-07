package com.onezol.platform.model.param;

public class DictEntryParam extends BaseParam {
    /**
     * 字典项名称，如：性别
     */
    private String entryName;
    /**
     * 字典项，如：GENDER
     */
    private String entryKey;
    /**
     * 备注
     */
    private String remark;

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEntryKey() {
        return entryKey;
    }

    public void setEntryKey(String entryKey) {
        this.entryKey = entryKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
