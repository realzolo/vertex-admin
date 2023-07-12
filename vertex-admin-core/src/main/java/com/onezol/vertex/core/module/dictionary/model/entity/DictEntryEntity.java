package com.onezol.vertex.core.module.dictionary.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.common.annotation.InsertStrategy;
import com.onezol.vertex.common.constant.enums.FieldStrategy;
import com.onezol.vertex.common.model.entity.BaseEntity;

@TableName("sys_dict_entry")
public class DictEntryEntity extends BaseEntity {
    /**
     * 字典项名称，如：性别
     */
    @InsertStrategy(FieldStrategy.UNIQUE)
    private String entryName;
    /**
     * 字典项，如：GENDER
     */
    @InsertStrategy(FieldStrategy.UNIQUE)
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
