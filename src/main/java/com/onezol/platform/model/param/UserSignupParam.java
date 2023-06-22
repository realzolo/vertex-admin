package com.onezol.platform.model.param;

import com.onezol.platform.annotation.Validator;

public class UserSignupParam {
    @Validator(required = true, minLength = 4, maxLength = 20)
    private String username;
    @Validator(required = true, minLength = 6, maxLength = 20)
    private String password;
    @Validator(required = true, regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
