package com.onezol.vertex.common.constant.enums;

/**
 * 枚举：消息分组
 */
public enum MessageGroup implements EnumService {
    DEFAULT("DEFAULT", 0),
    SITE("SITE", 1),
    SYSTEM("SYSTEM", 2),
    NOTICE("NOTICE", 3);

    private final String value;
    private final int code;

    MessageGroup(String value, int code) {
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
