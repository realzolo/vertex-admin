package com.onezol.vertex.core.model.dto;

import com.onezol.vertex.common.model.BaseDTO;

import java.time.LocalDateTime;

public class DictEntry extends BaseDTO {
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

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
