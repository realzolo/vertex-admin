package com.onezol.app;

import com.onezol.platform.service.DictValueService;
import com.onezol.platform.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest
class AppStarterTests {
    @Autowired
    private MailService mailService;
    @Autowired
    private DictValueService dictValueService;

    @Test
    void testSendEmail() {
        String to = "zolo@onezol.com";
        String subject = "Test Email";
        String content = "This is a test email.";
        try {
            mailService.sendMail(to, subject, content);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDictValueService() {
        dictValueService.getDictionary();
    }

}
