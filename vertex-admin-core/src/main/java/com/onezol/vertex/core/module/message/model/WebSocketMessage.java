package com.onezol.vertex.core.module.message.model;

import com.onezol.vertex.common.constant.DatePattern;
import com.onezol.vertex.common.constant.enums.WebSocketMessageGroup;
import com.onezol.vertex.common.constant.enums.WebSocketMessageType;
import com.onezol.vertex.common.util.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@NoArgsConstructor
public class WebSocketMessage<T> {
    /**
     * 消息组
     */
    @Setter
    private WebSocketMessageGroup group;
    /**
     * 消息事件类型
     */
    @Setter
    private WebSocketMessageType type;
    /**
     * 消息内容
     */
    @Setter
    @Getter
    private T content;
    /**
     * 消息发送时间
     */
    private LocalDateTime time = LocalDateTime.now();

    public WebSocketMessage(WebSocketMessageType type, T content) {
        this.group = WebSocketMessageGroup.DEFAULT;
        this.type = type;
        this.content = content;
        this.time = LocalDateTime.now();
    }

    public WebSocketMessage(WebSocketMessageGroup group, WebSocketMessageType type, T content) {
        this.group = group;
        this.type = type;
        this.content = content;
        this.time = LocalDateTime.now();
    }

    public static <T> WebSocketMessage<T> from(WebSocketMessage<T> message) {
        Assert.notNull(message, "WebSocketMessage must not be null");
        message.time = LocalDateTime.now();
        return message;
    }

    public static <T> WebSocketMessage<T> from(WebSocketMessage<?> message, Class<T> clazz) {
        Assert.notNull(message, "WebSocketMessage must not be null");
        message.time = LocalDateTime.now();
        return (WebSocketMessage<T>) message;
    }

    public int getGroup() {
        return group.getCode();
    }

    public int getType() {
        return type.getCode();
    }

    public String getTime() {
        return DateUtils.format(time, DatePattern.YYYY_MM_DD__HH_MM_SS);
    }
}
