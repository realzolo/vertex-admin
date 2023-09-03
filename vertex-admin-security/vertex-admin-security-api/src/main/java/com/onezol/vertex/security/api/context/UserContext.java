package com.onezol.vertex.security.api.context;

import com.onezol.vertex.security.api.model.UserIdentity;
import lombok.Data;

@Data
public class UserContext {
    private UserIdentity user;
}
