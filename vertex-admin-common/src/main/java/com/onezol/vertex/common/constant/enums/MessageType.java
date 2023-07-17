package com.onezol.vertex.common.constant.enums;

/**
 * 枚举：消息类型
 */
public enum MessageType implements EnumService {
    SYSTEM_MESSAGE("系统消息", 0),
    USER_MESSAGE("用户消息", 1),
    NOTICE("公告", 2),
    NOTIFICATION("通知", 3);

    private final String value;
    private final int code;

    MessageType(String value, int code) {
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
