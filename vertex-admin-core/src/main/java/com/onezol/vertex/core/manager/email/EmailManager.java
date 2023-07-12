package com.onezol.vertex.core.manager.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailManager {
    private static final Logger logger = LoggerFactory.getLogger(EmailManager.class);

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    public EmailManager() {
        this.mailSender = new JavaMailSenderImpl();
    }

    /**
     * 发送邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setHeader("Content-Transfer-Encoding", "base64");  // 防止乱码
            message.setHeader("Content-Type", "text/html;charset=UTF-8");
            message.setFrom(from);
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=UTF-8");
        } catch (MessagingException e) {
            logger.error("邮件[{} -> {}]: 发送失败", from, to, e);
            throw e;
        }
        mailSender.send(message);
        logger.info("邮件[{} -> {}]: 发送成功", from, to);
    }
}
