package com.onezol.platform.model.param;

import com.onezol.platform.annotation.Validator;

public class UserSigninParam {
    @Validator(required = true, minLength = 4, maxLength = 20)
    private String username;
    @Validator(required = true, minLength = 6, maxLength = 20)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
