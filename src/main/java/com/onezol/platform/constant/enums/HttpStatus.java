package com.onezol.platform.constant.enums;

import com.onezol.platform.constant.EnumService;

public enum HttpStatus implements EnumService {
    SUCCESS("成功", 10000),
    FAILURE("失败", 10101),
    BAD_PARAMETER("参数错误", 10102),
    UNAUTHORIZED("未认证", 10103),
    FORBIDDEN("无权限", 10104),
    NOT_FOUND("未找到", 10105);

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
