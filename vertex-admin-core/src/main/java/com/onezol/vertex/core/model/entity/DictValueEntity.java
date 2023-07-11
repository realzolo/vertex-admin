package com.onezol.vertex.core.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.common.annotation.InsertStrategy;
import com.onezol.vertex.common.constant.enums.FieldStrategy;
import com.onezol.vertex.core.common.model.entity.BaseEntity;

@TableName("pf_dict_value")
public class DictValueEntity extends BaseEntity {
    /**
     * 字典项ID
     */
    private Long entryId;
    /**
     * 字典值键，如：PERSON.GENDER
     */
    @InsertStrategy(FieldStrategy.UNIQUE)
    private String dictKey;
    /**
     * 字典代码，如：1
     */
    private Integer code;
    /**
     * 字典值，如：男
     */
    @InsertStrategy(FieldStrategy.UNIQUE)
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
