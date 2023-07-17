package com.onezol.vertex.core.module.message.endpoint;

import com.onezol.vertex.common.annotation.Anonymous;
import com.onezol.vertex.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@Anonymous
@ServerEndpoint("/ws/notification/{userId}")
@EnableScheduling
public class MessageSocketEndpoint {
    // session集合,存放对应的session
    private static final ConcurrentHashMap<Long, Session> sessions = new ConcurrentHashMap<>();
    // concurrent包的线程安全Set,用来存放每个客户端对应的WebSocket对象。
    private static final CopyOnWriteArraySet<MessageSocketEndpoint> sockets = new CopyOnWriteArraySet<>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 推送消息到指定用户
     */
    public static void sendMessage(Long userId, String message) {
        log.info("用户ID：" + userId + ",推送内容：" + message);
        Session session = sessions.get(userId);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("推送消息到指定用户发生错误：" + e.getMessage(), e);
        }
    }

    /**
     * 群发消息
     */
    public static void sendBroadcast(Object message) {
        String json = JsonUtils.toJson(message);
        log.info("发送消息：{}", message);
        for (MessageSocketEndpoint socket : sockets) {
            try {
                socket.session.getBasicRemote().sendText(json);
            } catch (IOException e) {
                log.error("群发消息发生错误：" + e.getMessage(), e);
            }
        }
    }

    /**
     * 建立WebSocket连接
     */
    @OnOpen
    @SuppressWarnings("SuspiciousMethodCalls")
    public void onOpen(Session session, @PathParam(value = "userId") Long userId) {
        log.info("WebSocket建立连接中,连接用户ID：{}", userId);
        try {
            final Session oldSession = sessions.get(userId);
            // oldSession不为空,说明已经有人登陆账号,应该删除登陆的WebSocket对象
            if (oldSession != null) {
                sockets.remove(oldSession);
                oldSession.close();
            }
        } catch (IOException e) {
            log.error("重复登录异常,错误信息：" + e.getMessage(), e);
        }
        // 建立连接
        this.session = session;
        sockets.add(this);
        sessions.put(userId, session);
        log.info("建立连接完成,当前在线人数为：{}", sockets.size());
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        sockets.remove(this);
        log.info("连接断开,当前在线人数为：{}", sockets.size());
    }

    /**
     * 接收客户端消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("收到客户端发来的消息：{}", message);
    }
}