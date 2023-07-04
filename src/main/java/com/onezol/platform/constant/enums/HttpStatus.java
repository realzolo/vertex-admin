package com.onezol.platform.constant.enums;

import com.onezol.platform.constant.EnumService;

public enum HttpStatus implements EnumService {
    SUCCESS("ok", 10000),
    FAILURE("failure", 10001),
    LOGIN_FAILURE("登录失败", 10002),
    NO_PERMISSION("无访问权限", 10003),
    FORBIDDEN("禁止访问", 10004),
    UNAUTHORIZED("未授权", 10005),
    PARAM_ERROR("请求参数错误", 10006);


    private final String value;
    private final int code;

    HttpStatus(String value, int code) {
        this.value = value;
        this.code = code;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public int getCode() {
        return code;
    }

}
