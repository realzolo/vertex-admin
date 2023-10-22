package com.onezol.vertex.common.constant.enums;

/**
 * 枚举：消息类型
 */
public enum WebSocketEventType implements EnumService {
    AUTHENTICATION("AUTHENTICATION", 0),
    HEARTBEAT("HEARTBEAT", 1),
    JOIN_GROUP("JOIN_GROUP", 3),
    LEAVE_GROUP("LEAVE_GROUP", 4),
    SESSION_ANALYSIS("SESSION_ANALYSIS", 5);

    private final String value;
    private final int code;

    WebSocketEventType(String value, int code) {
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
