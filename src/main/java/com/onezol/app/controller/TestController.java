package com.onezol.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/return-string")
    public String returnString() {
        return "pong";
    }

    @RequestMapping("/return-map")
    public Map<String,Object> returnMap() {
        return new HashMap<String,Object>(){{
            put("key","value");
            put("key2","value2");
        }};
    }
}
