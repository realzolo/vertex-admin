package com.onezol.vertex.core.module.message.endpoint;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.onezol.vertex.common.constant.enums.EnumService;
import com.onezol.vertex.common.constant.enums.MessageGroup;
import com.onezol.vertex.common.constant.enums.WebSocketEventType;
import com.onezol.vertex.common.util.CodecUtils;
import com.onezol.vertex.common.util.JsonUtils;
import com.onezol.vertex.common.util.JwtUtils;
import com.onezol.vertex.common.util.StringUtils;
import com.onezol.vertex.core.common.config.WebSocketConfig;
import com.onezol.vertex.core.common.manager.async.AsyncTaskManager;
import com.onezol.vertex.core.module.message.misc.WebSocketMessageEncoder;
import com.onezol.vertex.core.module.message.model.WebSocketMessage;
import com.onezol.vertex.core.module.message.model.WebSocketSessionEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@ServerEndpoint(value = "/websocket", configurator = WebSocketConfig.class, encoders = {WebSocketMessageEncoder.class})
@EnableScheduling
public class WebSocketEndpoint {
    private static final Map<String, List<WebSocketSessionEntry>> SESSION_GROUPS = new ConcurrentHashMap<>();
    private Session session;

    /**
     * 建立WebSocket连接
     */
    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        String token = (String) session.getUserProperties().get("Authorization");
        RemoteEndpoint.Basic endpoint = session.getBasicRemote();

        // 连接鉴权
        if (StringUtils.isBlank(token) || !JwtUtils.validateToken(token)) {
            WebSocketMessage<Boolean> message = new WebSocketMessage<>(WebSocketEventType.AUTHENTICATION, false);
            if (session.isOpen()) {
                endpoint.sendObject(message);
                closeSessionAsync(session);
            }
            return;
        }
        // 连接成功
        this.session = session;
        WebSocketMessage<Boolean> message = new WebSocketMessage<>(WebSocketEventType.AUTHENTICATION, true);
        endpoint.sendObject(message);

        // 将Session加入default session组
        String sessionKey = getSessionKey();
        SESSION_GROUPS.computeIfAbsent(MessageGroup.DEFAULT.getValue(), k -> new CopyOnWriteArrayList<>())
                .add(new WebSocketSessionEntry(sessionKey, session));

        // 群组变化通知
        sessionChangeNotice();
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        log.error("WebSocket发生错误：" + throwable.getMessage(), throwable);
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        String sessionKey = getSessionKey();
        // 将Session从Session组移除
        SESSION_GROUPS.forEach((k, v) -> v.removeIf(s -> s.getKey().equals(sessionKey)));
    }

    /**
     * 接收客户端消息
     */
    @OnMessage
    public void onMessage(String message) throws IOException, EncodeException {
        RemoteEndpoint.Basic endpoint = session.getBasicRemote();
        log.debug("收到客户端发来的消息：{}", message);
        WebSocketMessage<?> wsm = JsonUtils.fromJson(message, WebSocketMessage.class);
        String type = wsm.getType();
        String sessionKey = getSessionKey();
        // 心跳消息
        if (type.equals(WebSocketEventType.HEARTBEAT.getValue())) {
            log.debug("群组信息： {}", SESSION_GROUPS.size());
            WebSocketMessage<?> m = WebSocketMessage.from(wsm);
            endpoint.sendObject(m);
        }
        // 加入Session组
        else if (type.equals(WebSocketEventType.JOIN_GROUP.getValue())) {
            String group = ((String) wsm.getContent());
            // 判断是否已经加入该Session组
            if (SESSION_GROUPS.getOrDefault(group, Collections.emptyList()).stream().noneMatch(s -> s.getKey().equals(sessionKey))) {
                // 将Session加入指定的Session组
                SESSION_GROUPS.computeIfAbsent(group, k -> new CopyOnWriteArrayList<>())
                        .add(new WebSocketSessionEntry(sessionKey, session));
            }
            WebSocketMessage<Boolean> m = WebSocketMessage.from(wsm, Boolean.class);
            m.setGroup(EnumService.getEnumByValue(MessageGroup.class, group));
            m.setContent(true);
            endpoint.sendObject(m);

            sessionChangeNotice();
        }
        // 离开Session组
        else if (type.equals(WebSocketEventType.LEAVE_GROUP.getValue())) {
            String group = ((String) wsm.getContent());
            // 将Session从Session组移除
            SESSION_GROUPS.getOrDefault(group, Collections.emptyList()).removeIf(s -> s.getKey().equals(sessionKey));
            WebSocketMessage<Boolean> m = WebSocketMessage.from(wsm, Boolean.class);
            m.setGroup(EnumService.getEnumByValue(MessageGroup.class, group));
            m.setContent(true);
            endpoint.sendObject(m);

            sessionChangeNotice();
        }
    }

    /**
     * 获取Session唯一标识key（用户ID）
     *
     * @return 用户ID
     */
    private String getSessionKey() {
        String token = (String) session.getUserProperties().get("Authorization");
        String subject = JwtUtils.getSubjectFromToken(token);
        String idAndUsername = CodecUtils.decodeBase64(subject);
        return idAndUsername.split("@")[0];
    }

    public void sessionChangeNotice() {
        log.debug("---------------------------");
        JSONArray content = new JSONArray();
        for (Map.Entry<String, List<WebSocketSessionEntry>> entry : SESSION_GROUPS.entrySet()) {
            log.debug("群组： {}，人数： {}", entry.getKey(), entry.getValue().size());
            JSONObject item = new JSONObject() {{
                put("group", entry.getKey());
                put("size", entry.getValue().size());
            }};
            content.add(item);
        }

        WebSocketMessage<Object> message = new WebSocketMessage<>();
        message.setType(WebSocketEventType.SESSION_ANALYSIS);
        message.setGroup(MessageGroup.SYSTEM);
        message.setContent(content);

        SESSION_GROUPS.getOrDefault(MessageGroup.SYSTEM.getValue(), Collections.emptyList()).forEach(entry -> {
            try {
                entry.getValue().getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                log.error("WebSocket发送消息异常：" + e.getMessage(), e);
            }
        });
    }

    /**
     * 异步关闭会话
     *
     * @param session 会话
     */
    private void closeSessionAsync(Session session) {
        AsyncTaskManager.getInstance().execute(() -> {
            try {
                session.close();
            } catch (IOException e) {
                log.error("WebSocket认证失败，关闭连接异常：" + e.getMessage(), e);
            }
        }, 10000);
    }
}

