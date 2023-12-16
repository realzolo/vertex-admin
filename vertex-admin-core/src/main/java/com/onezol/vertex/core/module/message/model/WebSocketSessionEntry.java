package com.onezol.vertex.core.module.message.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.websocket.Session;

@Data
@AllArgsConstructor
public class WebSocketSessionEntry {

    private String key;

    private Session value;
}
