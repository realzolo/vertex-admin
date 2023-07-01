package com.onezol.platform.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.platform.annotation.InsertStrategy;
import com.onezol.platform.constant.enums.FieldStrategy;

@TableName("pf_dict_key")
public class DictKeyEntity extends BaseEntity {
    /**
     * 字典名称，如：性别
     */
    @InsertStrategy(FieldStrategy.UNIQUE)
    private String name;
    /**
     * 字典键，如：GENDER
     */
    @TableField("`key`")
    @InsertStrategy(FieldStrategy.UNIQUE)
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
