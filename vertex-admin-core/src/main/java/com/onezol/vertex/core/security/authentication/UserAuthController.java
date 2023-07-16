package com.onezol.vertex.core.security.authentication;

import com.onezol.vertex.common.annotation.Anonymous;
import com.onezol.vertex.core.security.authentication.model.UserLoginPayload;
import com.onezol.vertex.core.security.authentication.service.UserAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户认证相关接口
 */
@Anonymous
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