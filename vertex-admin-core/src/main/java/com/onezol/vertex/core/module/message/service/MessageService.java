package com.onezol.vertex.core.module.message.service;

import com.onezol.vertex.common.service.impl.GenericServiceImpl;
import com.onezol.vertex.core.common.util.ModelUtils;
import com.onezol.vertex.core.module.message.endpoint.MessageSocketEndpoint;
import com.onezol.vertex.core.module.message.mapper.MessageMapper;
import com.onezol.vertex.core.module.message.model.Message;
import com.onezol.vertex.core.module.message.model.MessageEntity;
import com.onezol.vertex.core.module.message.model.MessagePayload;
import com.onezol.vertex.security.api.model.UserIdentity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService extends GenericServiceImpl<MessageMapper, MessageEntity> {
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
            MessageSocketEndpoint.sendBroadcast(message);
        }
        return ok;
    }
}
