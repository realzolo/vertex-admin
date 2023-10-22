package com.onezol.vertex.core.module.message.handler;

import com.onezol.vertex.core.module.message.model.WebSocketSessionEntry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultWebSocketHandler implements WebSocketHandler {
    // 预鉴权的session
    private static final Map<String, WebSocketSession> preAuthSessions = new ConcurrentHashMap<>();

    // session集合,存放对应的session
    private static final Map<String, List<WebSocketSessionEntry>> sessionGroups = new ConcurrentHashMap<>();

    /**
     * 连接建立后处理
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String group = Objects.requireNonNull(session.getUri()).getPath().replace("/ws/", "");

        // 将session加入到预鉴权的session集合中
        String authorizationHeader = session.getHandshakeHeaders().getFirst("Authorization");
    }

    /**
     * 处理收到的消息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 处理收到的消息
        System.out.println("接收到消息：" + message.getPayload());
    }

    /**
     * 连接关闭后处理
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("WebSocket连接已关闭");
    }

    /**
     * supportsPartialMessages 为 true 时，可以只处理消息的一部分
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 处理传输错误
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("WebSocket错误: " + exception.getMessage());
    }

    private boolean validateToken(String authorizationHeader) {
        // 进行Token验证逻辑，验证Token的有效性
        // 若验证成功返回true，否则返回false
        // 示例实现中仅检查Token是否以 "Bearer " 开头
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }
}