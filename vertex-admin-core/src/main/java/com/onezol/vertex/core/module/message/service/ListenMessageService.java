package com.onezol.vertex.core.module.message.service;

import com.onezol.vertex.common.annotation.OnWebSocketMessage;
import com.onezol.vertex.common.util.JsonUtils;
import com.onezol.vertex.core.module.message.model.WebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class ListenMessageService {

    @OnWebSocketMessage(group = "DEFAULT", type = "*")
    public void handleDefaultMessage(WebSocketSession session, WebSocketMessage<String> webSocketMessage) {
        log.info("[DEFAULT] - [*]: " + JsonUtils.toJson(webSocketMessage));
    }

    @OnWebSocketMessage(group = "TASK", type = "*")
    public void handleTaskMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) {
        log.info("[TASK] - [*]: " + JsonUtils.toJson(webSocketMessage));
    }

    @OnWebSocketMessage(group = "SYSTEM", type = "GROUP_CHANGE")
    public void handleSystemMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) {
        log.info("[SYSTEM] - [*]: " + JsonUtils.toJson(webSocketMessage));
    }
}
