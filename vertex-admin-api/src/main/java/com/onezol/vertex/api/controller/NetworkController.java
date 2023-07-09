package com.onezol.vertex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
