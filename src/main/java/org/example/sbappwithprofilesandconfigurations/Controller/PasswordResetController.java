package org.example.sbappwithprofilesandconfigurations.Controller;

import org.example.sbappwithprofilesandconfigurations.Service.PasswordResetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class PasswordResetController {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);
    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        logger.info("Password reset request for email: {}", email);
        try {
            passwordResetService.requestPasswordReset(email);
            return ResponseEntity.ok("Password reset link sent to your email");
        } catch (Exception e) {
            logger.error("Failed to request password reset for email {}: {}", email, e.getMessage());
            return ResponseEntity.status(500).body("Failed to send password reset link");
        }

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        logger.info("Resetting password for token: {}", token);
        try {
            passwordResetService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password successfully reset");
        } catch (Exception e) {
            logger.error("Failed to reset password for token {}: {}", token, e.getMessage());
            return ResponseEntity.status(500).body("Failed to reset password");
        }
    }
}
