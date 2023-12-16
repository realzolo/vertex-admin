package com.onezol.vertex.common.constant.enums;

/**
 * 枚举：消息类型
 */
public enum WebSocketMessageType implements EnumService {
    /**
     * 默认消息
     */
    DEFAULT("DEFAULT", 0),
    /**
     * 认证消息
     */
    AUTHENTICATION("AUTHENTICATION", 1),
    /**
     * 心跳消息
     */
    HEARTBEAT("HEARTBEAT", 2),
    /**
     * 加入分组
     */
    JOIN_GROUP("JOIN_GROUP", 3),
    /**
     * 离开分组
     */
    LEAVE_GROUP("LEAVE_GROUP", 4),
    /**
     * 分组变更
     */
    GROUP_CHANGE("GROUP_CHANGE", 5);

    private final String value;
    private final int code;

    WebSocketMessageType(String value, int code) {
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
