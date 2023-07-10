package com.onezol.vertex.security.controller;

import com.onezol.vertex.security.model.param.UserLoginParam;
import com.onezol.vertex.security.service.UserAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户认证相关接口
 */
@RestController
@RequestMapping("/auth")
public class UserAuthController {
    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody UserLoginParam param) {
        return userAuthService.login(param.getUsername(), param.getPassword(), param.getCaptcha());
    }
}
