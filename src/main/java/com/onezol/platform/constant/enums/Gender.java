package com.onezol.platform.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.platform.constant.EnumService;

/**
 * 性别枚举
 */
public enum Gender implements EnumService {
    MALE(0, "男"),
    FEMALE(1, "女");
    @EnumValue
    private final int code;
    private final String value;

    Gender(int code, String value) {
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
