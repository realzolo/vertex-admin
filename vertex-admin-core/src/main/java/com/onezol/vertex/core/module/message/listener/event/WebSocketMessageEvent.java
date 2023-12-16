package com.onezol.vertex.core.module.message.listener.event;

import com.onezol.vertex.core.module.message.model.WebSocketMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class WebSocketMessageEvent extends ApplicationEvent {
    private final WebSocketSession session;
    private final WebSocketMessage<?> webSocketMessage;

    public WebSocketMessageEvent(Object source, WebSocketSession session, WebSocketMessage<?> webSocketMessage) {
        super(source);
        this.session = session;
        this.webSocketMessage = webSocketMessage;
    }
}