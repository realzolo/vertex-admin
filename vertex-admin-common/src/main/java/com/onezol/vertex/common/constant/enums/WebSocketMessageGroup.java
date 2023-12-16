package com.onezol.vertex.common.constant.enums;

/**
 * 枚举：WebSocket消息组
 */
public enum WebSocketMessageGroup implements EnumService {
    /**
     * 默认分组
     */
    DEFAULT("DEFAULT", 0),
    /**
     * 任务分组：用于任务通信
     */
    TASK("TASK", 2),
    /**
     * 系统分组：用于系统内部通信
     */
    SYSTEM("SYSTEM", 3);

    private final String value;
    private final int code;

    WebSocketMessageGroup(String value, int code) {
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
