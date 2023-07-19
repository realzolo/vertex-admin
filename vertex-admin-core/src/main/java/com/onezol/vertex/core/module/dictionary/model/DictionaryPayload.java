package com.onezol.vertex.core.module.dictionary.model;

import com.onezol.vertex.common.model.payload.BasePayload;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DictionaryPayload extends BasePayload {
    private String dictKey;

    private String dictValue;

    private Integer dictCode;

    private Long parentId;

    private String remark;
}
