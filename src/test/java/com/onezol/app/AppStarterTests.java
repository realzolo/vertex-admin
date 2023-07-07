package com.onezol.app;

import com.onezol.platform.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppStarterTests {
    @Autowired
    private MailService mailService;

    @Test
    void testSendEmail() {
        String to = "zolo@onezol.com";
        String subject = "Test Email";
        String content = "This is a test email.";
        mailService.sendMail(to, subject, content);
    }

}
