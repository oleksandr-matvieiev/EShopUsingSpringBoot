package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service

public class PasswordResetService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private final UserRepo userRepo;
    private final EmailService emailService;
    private final PasswordEncoder encoder;

    public PasswordResetService(UserRepo userRepo, EmailService emailService, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.encoder = encoder;
    }

    public void requestPasswordReset(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found for email: {}", email);
                    return new IllegalArgumentException("User not found");
                });

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(1));
        userRepo.save(user);

        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        logger.info("Password reset request for user with email {} send", email);
    }
    public void resetPassword(String resetToken, String newPassword) {
        User user = userRepo.findByResetToken(resetToken).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            logger.error("Invalid reset token {}", resetToken);
            throw new IllegalArgumentException("Token has expired");
        }
        user.setPassword(encoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiryDate(null);
        userRepo.save(user);
        logger.info("Password successfully reset for user with email {}", user.getEmail());

    }
}
