package com.onezol.app;

import com.onezol.platform.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppStarterTests {
    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail() {
        String to = "zolo@onezol.com";
        String subject = "Test Email";
        String content = "This is a test email.";
        emailService.sendEmail(to, subject, content);
    }

}
