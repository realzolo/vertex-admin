package com.onezol.platform.controller;

import com.onezol.platform.model.pojo.ListQueryResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/return-string")
    public String returnString() {
        return "pong";
    }

    @RequestMapping("/return-map")
    public Map<String, Object> returnMap() {
        return new HashMap<String, Object>() {{
            put("key", "value");
            put("key2", "value2");
        }};
    }

    @RequestMapping("/return-list")
    public List<Object> returnList() {
        return Arrays.asList("Zolo", "Li", "T", 123);
    }

    @RequestMapping("/return-object")
    public Object returnObject() {
        ListQueryResult<Object> result = new ListQueryResult<>();
        result.setItems(Arrays.asList("Zolo", "Li", "T", 123).toArray());
        result.setTotal(999L);
        return result;
    }
}
