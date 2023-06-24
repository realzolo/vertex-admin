package com.onezol.platform.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("pf_dict_key")
public class DictKeyEntity extends BaseEntity {
    /**
     * 字典名称，如：性别
     */
    private String name;
    /**
     * 字典键，如：GENDER
     */
    @TableField("`key`")
    private String key;
    /**
     * 字典描述
     */
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
