package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service

public class PasswordResetService {
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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(1));
        userRepo.save(user);
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepo.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }
        user.setPassword(encoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiryDate(null);
        userRepo.save(user);

    }
}
