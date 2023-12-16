package com.onezol.vertex.security.api.model.payload;

import com.onezol.vertex.common.model.payload.BasePayload;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleMenuPayload extends BasePayload {

    private Long roleId;

    private Long[] menuIds;
}
