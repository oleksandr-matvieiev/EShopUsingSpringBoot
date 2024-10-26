package org.example.sbappwithprofilesandconfigurations.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendPasswordResetEmail_Success() {
        String email = "test@example.com";
        String resetLink = "http://localhost/reset?token=abc123";

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendPasswordResetEmail(email, resetLink);

        verify(javaMailSender, times(1)).send(argThat((SimpleMailMessage message) ->
                message.getTo()[0].equals(email) &&
                        message.getSubject().equals("Password reset request") &&
                        message.getText().contains(resetLink)
        ));
    }

    @Test
    void sendPasswordResetEmail_ExceptionHandling() {
        String email = "test@example.com";
        String resetLink = "http://localhost/reset?token=abc123";

        doThrow(new RuntimeException("Email server not available")).when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendPasswordResetEmail(email, resetLink);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

    }
}
