package com.onezol.vertex.security.api.model.dto;

import com.onezol.vertex.common.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseDTO {

    private String name;

    private String key;

    private String remark;
}
