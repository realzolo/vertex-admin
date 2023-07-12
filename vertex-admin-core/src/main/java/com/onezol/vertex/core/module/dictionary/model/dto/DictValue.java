package com.onezol.vertex.core.module.dictionary.model.dto;

import com.onezol.vertex.common.model.dto.BaseDTO;

import java.time.LocalDateTime;

public class DictValue extends BaseDTO {
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

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
