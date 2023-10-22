package com.onezol.vertex.core.module.message.misc;

import com.onezol.vertex.common.util.JsonUtils;
import com.onezol.vertex.core.module.message.model.WebSocketMessage;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class WebSocketMessageEncoder implements Encoder.Text<WebSocketMessage> {

    @Override
    public String encode(WebSocketMessage message) {
        return JsonUtils.toJson(message);
    }

    @Override
    public void init(EndpointConfig config) {
        config.getUserProperties().put("encoder", this);
    }

    @Override
    public void destroy() {
    }
}