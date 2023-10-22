package com.onezol.vertex.core.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 用于修改握手请求和响应参数
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        Map<String, Object> userProperties = sec.getUserProperties();
        Map<String, List<String>> headers = request.getHeaders();
        List<String> authorization = headers.get("Sec-WebSocket-Protocol");
        if (!CollectionUtils.isEmpty(authorization)) {
            userProperties.put("Authorization", authorization.get(0));
            response.getHeaders().put("Sec-WebSocket-Protocol", Collections.singletonList(authorization.get(0)));
        }
    }
}