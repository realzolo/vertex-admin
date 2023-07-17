package com.onezol.vertex.core.module.message.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_message")
public class MessageEntity extends BaseEntity {
    /**
     * 用户(发送者)ID
     */
    private Long userId;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否全部可见
     */
    private Boolean allVisible;

    /**
     * 接收者ID
     */
    private Long[] receiverIds;

    /**
     * 定时发布
     */
    private Boolean timing;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 是否可见
     */
    private Boolean visible;
}
