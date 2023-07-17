package com.onezol.vertex.core.module.message.mapper;

import com.onezol.vertex.core.base.mapper.BaseMapper;
import com.onezol.vertex.core.module.message.model.MessageEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<MessageEntity> {
}
