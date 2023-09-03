package com.onezol.vertex.common.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 人员性别
 */
@Getter
@AllArgsConstructor
public enum Gender implements EnumService {
    FEMALE(0, "女"),
    MALE(1, "男");

    @EnumValue
    private final int code;
    private final String value;
}