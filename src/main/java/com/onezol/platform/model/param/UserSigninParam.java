package com.onezol.platform.model.param;

import com.onezol.platform.annotation.Validator;

public class UserSigninParam {
    @Validator(required = true)
    private String type;
    @Validator(minLength = 4, maxLength = 20)
    private String username;
    @Validator(minLength = 6, maxLength = 20)
    private String password;
    @Validator(regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")
    private String email;
    @Validator(regex = "^1[34578]\\d{9}$")
    private String captcha;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
