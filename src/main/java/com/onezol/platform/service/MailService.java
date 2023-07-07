package com.onezol.platform.service;

import javax.mail.MessagingException;

public interface MailService {
    /**
     * 发送邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendMail(String to, String subject, String content) throws MessagingException;
}
