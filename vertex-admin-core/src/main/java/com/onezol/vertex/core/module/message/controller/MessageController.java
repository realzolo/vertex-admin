package com.onezol.vertex.core.module.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.onezol.vertex.common.annotation.RestResponse;
import com.onezol.vertex.common.controller.BaseController;
import com.onezol.vertex.common.model.record.PlainPage;
import com.onezol.vertex.core.module.message.model.Message;
import com.onezol.vertex.core.module.message.model.MessageEntity;
import com.onezol.vertex.core.module.message.service.MessageService;
import com.onezol.vertex.security.api.annotation.RestrictAccess;
import com.onezol.vertex.security.api.context.UserContextHolder;
import com.onezol.vertex.security.api.model.dto.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "消息管理")
@RestrictAccess
@RestResponse
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController<MessageEntity> {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "获取消息列表")
    @GetMapping("/list")
    public PlainPage<Message> listAllMessages(
            @RequestParam(value = "pageNo", required = false) Long pageNo,
            @RequestParam(value = "pageSize", required = false) Long pageSize
    ) {
        IPage<MessageEntity> page = getPage(pageNo, pageSize);

        return messageService.listMessages(page, null);
    }

    @Operation(summary = "获取用户消息列表")
    @GetMapping("/user-message")
    public PlainPage<Message> listUserMessages(
            @RequestParam(value = "pageNo", required = false) Long pageNo,
            @RequestParam(value = "pageSize", required = false) Long pageSize
    ) {
        IPage<MessageEntity> page = getPage(pageNo, pageSize);

        User user = UserContextHolder.getContext().getUserDetails();
        return messageService.listMessages(page, user.getId());
    }
}
