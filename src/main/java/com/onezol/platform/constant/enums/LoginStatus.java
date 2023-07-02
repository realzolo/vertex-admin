package com.onezol.platform.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.platform.constant.EnumService;

public enum LoginStatus implements EnumService {
    SUCCESS(0, "登录成功"),
    PASSWORD_ERROR(1, "密码错误"),
    CAPTCHA_ERROR(2, "验证码错误"),
    ACCOUNT_DISABLED(3, "账户禁用"),
    ACCOUNT_LOCKED(4, "账户锁定");

    @EnumValue
    private final int code;
    private final String value;

    LoginStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }
}
