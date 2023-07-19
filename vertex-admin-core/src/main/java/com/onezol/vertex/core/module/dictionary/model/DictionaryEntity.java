package com.onezol.vertex.core.module.dictionary.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dictionary")
public class DictionaryEntity extends BaseEntity {

    private String dictKey;

    private String dictValue;

    private Integer dictCode;

    private Long parentId;

    private String remark;
}
