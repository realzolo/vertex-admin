package com.onezol.vertex.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 账户状态枚举
 */
public enum AccountStatus implements EnumService {
    ACTIVE(0, "正常"),
    LOCKED(1, "锁定"),
    DISABLED(2, "禁用"),
    EXPIRED(3, "过期");

    @EnumValue
    private final int code;
    private final String value;

    AccountStatus(int code, String value) {
        this.code = code;
        this.value = value;
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
