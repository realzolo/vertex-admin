package com.onezol.vertex.common.constant.enums;

/**
 * 枚举：可见范围
 */
public enum VisibleRange implements EnumService {
    ALL_VISIBLE("全部可见", 0),
    PART_VISIBLE("部分可见", 1);

    private final String value;
    private final int code;

    VisibleRange(String value, int code) {
        this.value = value;
        this.code = code;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
