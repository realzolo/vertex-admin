package com.onezol.vertex.security.api.model.payload;


import lombok.Data;

@Data
public class UserLoginPayload {
    private String username;
    private String password;
    private String email;
    private String captcha;
}