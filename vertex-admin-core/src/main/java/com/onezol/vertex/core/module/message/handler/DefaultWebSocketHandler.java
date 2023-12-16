package com.onezol.vertex.core.module.message.handler;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.onezol.vertex.common.constant.enums.EnumService;
import com.onezol.vertex.common.constant.enums.WebSocketMessageGroup;
import com.onezol.vertex.common.constant.enums.WebSocketMessageType;
import com.onezol.vertex.common.util.JsonUtils;
import com.onezol.vertex.common.util.JwtUtils;
import com.onezol.vertex.common.util.StringUtils;
import com.onezol.vertex.core.common.manager.async.AsyncTaskManager;
import com.onezol.vertex.core.module.message.listener.event.WebSocketMessageEvent;
import com.onezol.vertex.core.module.message.model.WebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DefaultWebSocketHandler extends TextWebSocketHandler implements WebSocketHandler {
    private static final Map<String, Set<WebSocketSession>> SESSION_GROUPS = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher applicationEventPublisher;

    public DefaultWebSocketHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 连接建立
     *
     * @param session websocket session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 身份认证
        if (!authenticate(session)) {
            return;
        }

        // 加入默认session组
        SESSION_GROUPS.computeIfAbsent(WebSocketMessageGroup.DEFAULT.getValue(), k -> ConcurrentHashMap.newKeySet()).add(session);

        // 响应认证成功
        this.sendMessage(session, WebSocketMessageGroup.DEFAULT, WebSocketMessageType.AUTHENTICATION, true);
    }

    /**
     * 处理消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 解析消息
        WebSocketMessage<?> webSocketMessage;
        try {
            webSocketMessage = JsonUtils.fromJson(message.getPayload(), WebSocketMessage.class);
        } catch (JSONException e) {
            this.sendMessage(session, WebSocketMessageGroup.DEFAULT, WebSocketMessageType.DEFAULT, "消息格式错误, 无法处理！");
            return;
        }

        // 处理消息
        String group = Objects.requireNonNull(EnumService.getEnumByCode(WebSocketMessageGroup.class, webSocketMessage.getGroup())).getValue();
        String type = Objects.requireNonNull(EnumService.getEnumByCode(WebSocketMessageType.class, webSocketMessage.getType())).getValue();
        Object content = webSocketMessage.getContent();
        switch (type) {
            case "HEARTBEAT":  // 心跳
                this.sendMessage(session, WebSocketMessageGroup.DEFAULT, WebSocketMessageType.HEARTBEAT, null);
                break;
            case "JOIN_GROUP": // 加入群组
                if (Objects.isNull(content)) {
                    return;
                }
                SESSION_GROUPS.computeIfAbsent(group, k -> ConcurrentHashMap.newKeySet()).add(session);
                this.sessionChangeNotice();
                break;
            case "LEAVE_GROUP": // 离开群组
                if (Objects.isNull(content)) {
                    return;
                }
                SESSION_GROUPS.computeIfAbsent(group, k -> ConcurrentHashMap.newKeySet()).remove(session);
                this.sessionChangeNotice();
                break;
            default:
                WebSocketMessageEvent event = new WebSocketMessageEvent(this, session, webSocketMessage);
                applicationEventPublisher.publishEvent(event);
                break;
        }
    }

    /**
     * 连接关闭
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 从Session组移除
        SESSION_GROUPS.forEach((k, v) -> v.removeIf(s -> s.getId().equals(session.getId())));
        // 群组变化通知
        this.sessionChangeNotice();
    }

    /**
     * 身份认证
     *
     * @param session 会话
     * @return 认证结果
     */
    private boolean authenticate(WebSocketSession session) {
        HttpHeaders handshakeHeaders = session.getHandshakeHeaders();
        List<String> protocols = handshakeHeaders.get("Sec-WebSocket-Protocol");
        if (Objects.isNull(protocols) || protocols.isEmpty()) {
            closeSessionAsync(session);
            return false;
        }
        String token = protocols.get(0);
        if (StringUtils.isBlank(token) || !JwtUtils.validateToken(token)) {
            closeSessionAsync(session);
            return false;
        }
        return true;
    }

    /**
     * 发送消息
     *
     * @param session 会话
     * @param group   组
     * @param type    类型
     * @param content 内容
     */
    private void sendMessage(WebSocketSession session, WebSocketMessageGroup group, WebSocketMessageType type, Object content) throws IOException {
        if (Objects.isNull(session) || !session.isOpen()) {
            throw new IOException("无法发送消息，当前会话不存在或已关闭");
        }
        WebSocketMessage<Object> webSocketMessage = new WebSocketMessage<>(group, type, content);
        String text = JsonUtils.toJson(webSocketMessage);
        session.sendMessage(new TextMessage(text));
    }

    /**
     * 群组变化通知<br/>
     * 通知所有系统[SYSTEM]组的会话，消息类型为：SESSION_CHANGE
     */
    private void sessionChangeNotice() {
        log.debug("---------------------------");
        JSONArray content = new JSONArray();
        for (Map.Entry<String, Set<WebSocketSession>> entry : SESSION_GROUPS.entrySet()) {
            log.debug("群组： {}，人数： {}", entry.getKey(), entry.getValue().size());
            JSONObject item = new JSONObject() {{
                put("group", entry.getKey());
                put("size", entry.getValue().size());
            }};
            content.add(item);
        }

        WebSocketMessage<JSONArray> message = new WebSocketMessage<>();
        message.setGroup(WebSocketMessageGroup.SYSTEM);
        message.setType(WebSocketMessageType.GROUP_CHANGE);
        message.setContent(content);

        SESSION_GROUPS.getOrDefault(WebSocketMessageGroup.SYSTEM.getValue(), Collections.emptySet()).forEach(entry -> {
            try {
                entry.sendMessage(new TextMessage(JsonUtils.toJson(message)));
            } catch (IOException e) {
                log.error("WebSocket发送消息异常：" + e.getMessage(), e);
            }
        });
    }

    /**
     * 异步关闭会话
     *
     * @param session 会话
     */
    private void closeSessionAsync(WebSocketSession session) {
        AsyncTaskManager.getInstance().execute(() -> {
            try {
                session.close();
            } catch (IOException e) {
                log.error("WebSocket认证失败，关闭连接异常：" + e.getMessage(), e);
            }
        }, 10000);
    }
}