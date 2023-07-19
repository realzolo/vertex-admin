package com.onezol.vertex.app.controller;

import com.onezol.vertex.common.annotation.Loggable;
import com.onezol.vertex.common.model.dto.DTO;
import com.onezol.vertex.common.model.payload.GenericPayload;
import com.onezol.vertex.common.model.record.ListResultWrapper;
import com.onezol.vertex.core.base.controller.GenericController;
import com.onezol.vertex.core.module.message.model.Message;
import com.onezol.vertex.core.module.message.model.MessagePayload;
import com.onezol.vertex.core.module.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "通知公告")
@Loggable
@RestController
@RequestMapping("/message")
public class MessageController extends GenericController<MessageService> {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    @Operation(summary = "查询消息列表")
    @PostMapping("/list")
    protected ListResultWrapper<? extends DTO> queryList(@RequestBody GenericPayload payload) {
        return super.queryList(payload);
    }

    @Override
    @Operation(summary = "查询消息详情")
    @PostMapping("/query")
    protected DTO queryById(@RequestBody GenericPayload payload) {
        return super.queryById(payload);
    }

    @Operation(summary = "保存/更新消息")
    @PostMapping("/save")
    protected boolean createOrUpdate(@RequestBody MessagePayload payload) {
        return messageService.saveOrUpdate(payload);
    }

    @Override
    @Operation(summary = "删除消息")
    @PostMapping("/delete")
    protected boolean delete(@RequestBody GenericPayload payload) {
        return super.delete(payload);
    }

    @Override
    protected Class<? extends DTO> getDtoClass() {
        return Message.class;
    }
}
