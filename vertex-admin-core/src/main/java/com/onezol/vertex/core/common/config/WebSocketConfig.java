package com.onezol.vertex.core.common.config;

import com.onezol.vertex.core.module.message.handler.DefaultWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final DefaultWebSocketHandler defaultWebSocketHandler;

    public WebSocketConfig(DefaultWebSocketHandler defaultWebSocketHandler) {
        this.defaultWebSocketHandler = defaultWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(defaultWebSocketHandler, "/websocket").setAllowedOrigins("*");
    }
}