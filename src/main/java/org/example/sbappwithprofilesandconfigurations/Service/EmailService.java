package org.example.sbappwithprofilesandconfigurations.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendPasswordResetEmail(String email, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(email);
            message.setSubject("Password reset request");
            message.setText("To reset your password, click the following link: " + resetLink);
            javaMailSender.send(message);
            logger.info("Password reset message send to {}", email);
        } catch (Exception e) {
            logger.error("Error while sending password reset email to {}: {}", email, e.getMessage());
        }
    }
}
