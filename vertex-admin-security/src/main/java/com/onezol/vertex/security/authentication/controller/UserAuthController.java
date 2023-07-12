package com.onezol.vertex.security.authentication.controller;

import com.onezol.vertex.security.authentication.service.UserAuthService;
import com.onezol.vertex.security.management.model.payload.UserLoginPayload;
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
    public Map<String, Object> login(@RequestBody UserLoginPayload payload) {
        return userAuthService.login(payload.getUsername(), payload.getPassword(), payload.getCaptcha());
    }
}
