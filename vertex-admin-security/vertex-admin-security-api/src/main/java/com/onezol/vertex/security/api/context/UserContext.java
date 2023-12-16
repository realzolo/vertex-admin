package com.onezol.vertex.security.api.context;

import com.onezol.vertex.security.api.model.dto.User;
import lombok.Data;

@Data
public class UserContext {
    private User userDetails;
}
