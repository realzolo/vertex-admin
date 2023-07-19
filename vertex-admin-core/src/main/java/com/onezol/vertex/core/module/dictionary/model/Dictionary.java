package com.onezol.vertex.core.module.dictionary.model;

import com.onezol.vertex.common.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Dictionary extends BaseDTO {
    private String dictKey;

    private String dictValue;

    private Integer dictCode;

    private Long parentId;

    private String remark;
}
