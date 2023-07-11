package com.onezol.vertex.core.model.param;

import com.onezol.vertex.core.common.model.param.BaseParam;

public class DictValueParam extends BaseParam {
    /**
     * 字典项ID
     */
    private Long entryId;
    /**
     * 字典键，如：GENDER
     */
    private String dictKey;
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

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
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
