package com.onezol.vertex.security.biz.controller;

import com.onezol.vertex.common.annotation.RestResponse;
import com.onezol.vertex.common.controller.BaseController;
import com.onezol.vertex.security.api.annotation.RestrictAccess;
import com.onezol.vertex.security.api.service.OnlineUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestrictAccess
@RestResponse
@RestController
@RequestMapping("/online-user")
public class OnlineUserController extends BaseController {
    private final OnlineUserService onlineUserService;

    public OnlineUserController(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @RequestMapping("/list")
    public Object list(
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return onlineUserService.getOnlineUsers(pageNo, pageSize);
    }
}
