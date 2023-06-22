package com.onezol.platform.controller;

import com.onezol.platform.annotation.Validated;
import com.onezol.platform.model.dto.User;
import com.onezol.platform.model.param.UserSigninParam;
import com.onezol.platform.model.param.UserSignupParam;
import com.onezol.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public User signup(@RequestBody @Validated UserSignupParam param) {
        return userService.signup(param);
    }

    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestBody @Validated UserSigninParam param) {
        return userService.signin(param.getUsername(), param.getPassword());
    }
}
