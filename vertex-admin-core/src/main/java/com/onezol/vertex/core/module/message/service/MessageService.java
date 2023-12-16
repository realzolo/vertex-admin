package com.onezol.vertex.core.module.message.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.model.record.PlainPage;
import com.onezol.vertex.common.service.impl.BaseServiceImpl;
import com.onezol.vertex.core.common.util.ModelUtils;
import com.onezol.vertex.core.module.message.mapper.MessageMapper;
import com.onezol.vertex.core.module.message.model.Message;
import com.onezol.vertex.core.module.message.model.MessageEntity;
import com.onezol.vertex.core.module.message.model.MessagePayload;
import com.onezol.vertex.security.api.model.UserIdentity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class MessageService extends BaseServiceImpl<MessageMapper, MessageEntity> {

    private final MessageMapper messageMapper;

    public MessageService(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    /**
     * 获取分页消息列表
     *
     * @param page   分页对象
     * @param userId 用户ID. 为null时查询全部消息
     */
    public PlainPage<Message> listMessages(IPage<MessageEntity> page, Long userId) {
        Wrapper<MessageEntity> wrapper = Wrappers.<MessageEntity>lambdaQuery()
                .eq(Objects.nonNull(userId), MessageEntity::getUserId, userId);
        IPage<MessageEntity> resultPage = this.page(page, wrapper);

        return PlainPage.from(resultPage, Message.class);
    }

    /**
     * 保存或更新消息
     *
     * @param payload 消息载荷
     * @return 是否成功
     */
    @Transactional
    public boolean saveOrUpdate(MessagePayload payload) {
        if (payload == null) {
            return false;
        }
        MessageEntity entity = ModelUtils.convert(payload, MessageEntity.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserIdentity user = (UserIdentity) authentication.getPrincipal();
        assert entity != null;
        entity.setUserId(user.getUser().getId());
        boolean ok = saveOrUpdate(entity);
        // 如果是新增消息，则发送广播
        if (payload.getId() == null && ok) {
            Message message = ModelUtils.convert(entity, Message.class);
//            WebSocketEndpoint.sendBroadcast(message);
        }
        return ok;
    }
}
