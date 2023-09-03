package com.onezol.vertex.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户状态枚举
 */
@Getter
@AllArgsConstructor
public enum AccountStatus implements EnumService {
    ACTIVE(0, "正常"),
    LOCKED(1, "锁定"),
    DISABLED(2, "禁用"),
    EXPIRED(3, "过期");

    @EnumValue
    private final int code;
    private final String value;
}
