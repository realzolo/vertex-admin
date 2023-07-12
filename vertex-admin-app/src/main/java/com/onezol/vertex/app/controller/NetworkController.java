package com.onezol.vertex.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
@RequestMapping("/network")
public class NetworkController {
    /**
     * 检测网络是否通畅
     */
    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }
}
