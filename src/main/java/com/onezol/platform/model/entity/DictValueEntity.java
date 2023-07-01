package com.onezol.platform.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.platform.annotation.InsertStrategy;
import com.onezol.platform.constant.enums.FieldStrategy;


@TableName("pf_dict_value")
public class DictValueEntity extends BaseEntity {
    /**
     * 字典键ID
     */
    private Long keyId;
    /**
     * 字典值键，如：PERSON.GENDER
     */
    @TableField("`key`")
    @InsertStrategy(FieldStrategy.UNIQUE)
    private String key;
    /**
     * 字典代码，如：1
     */
    @InsertStrategy(FieldStrategy.UNIQUE)
    private Integer code;
    /**
     * 字典值，如：男
     */
    @InsertStrategy(FieldStrategy.UNIQUE)
    private String value;
    /**
     * 字典描述
     */
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
