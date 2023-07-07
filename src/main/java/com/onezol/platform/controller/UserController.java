package com.onezol.platform.controller;

import com.onezol.platform.annotation.Validated;
import com.onezol.platform.exception.BusinessException;
import com.onezol.platform.model.dto.User;
import com.onezol.platform.model.param.UserSigninParam;
import com.onezol.platform.model.param.UserSignupParam;
import com.onezol.platform.service.UserService;
import com.onezol.platform.util.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        String type = param.getType();
        switch (type) {
            case "account":
                if (param.getUsername() == null || param.getPassword() == null) {
                    throw new BusinessException("用户名或密码不能为空");
                }
                return userService.signinByAccount(param.getUsername(), param.getPassword());
            case "email":
                if (param.getEmail() == null || param.getCaptcha() == null) {
                    throw new BusinessException("邮箱或验证码不能为空");
                }
                return userService.signinByEmail(param.getEmail(), param.getCaptcha());
            default:
                throw new BusinessException("不支持的登录类型");
        }
    }

    @PostMapping("/send-email-code/{email}")
    public void sendEmailCode(@PathVariable String email) {
        boolean ok = RegexUtils.isEmail(email);
        if (!ok) {
            throw new BusinessException("邮箱格式不正确");
        }
        userService.sendEmailCode(email);
    }
}
