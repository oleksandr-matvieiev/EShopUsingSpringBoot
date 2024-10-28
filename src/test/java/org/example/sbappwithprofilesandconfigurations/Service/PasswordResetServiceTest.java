package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Exception.InvalidTokenException;
import org.example.sbappwithprofilesandconfigurations.Exception.UserNotFoundException;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PasswordResetServiceTest {
    @Mock
    private UserRepo userRepo;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetService passwordResetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void requestPasswordReset_Success() {
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(mockUser));

        passwordResetService.requestPasswordReset(email);

        assertNotNull(mockUser.getResetToken());
        assertNotNull(mockUser.getTokenExpiryDate());
        assertTrue(mockUser.getTokenExpiryDate().isAfter(LocalDateTime.now()));

        verify(userRepo, times(1)).save(mockUser);
        verify(emailService, times(1)).sendPasswordResetEmail(eq(email), contains("http://localhost:3000/reset-password?token="));
    }
    @Test
    void requestPasswordReset_UserNotFound() {
        String email = "nonexistent@example.com";

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> passwordResetService.requestPasswordReset(email));

        verify(userRepo, never()).save(any(User.class));
        verify(emailService, never()).sendPasswordResetEmail(anyString(), anyString());
    }

    @Test
    void resetPassword_Success() {
        String resetToken = UUID.randomUUID().toString();
        String newPassword = "newPassword";
        User mockUser = new User();
        mockUser.setResetToken(resetToken);
        mockUser.setTokenExpiryDate(LocalDateTime.now().plusMinutes(10));

        when(userRepo.findByResetToken(resetToken)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        passwordResetService.resetPassword(resetToken, newPassword);

        assertEquals("encodedPassword", mockUser.getPassword());
        assertNull(mockUser.getResetToken());
        assertNull(mockUser.getTokenExpiryDate());

        verify(userRepo, times(1)).save(mockUser);
        verify(passwordEncoder, times(1)).encode(newPassword);
    }

    @Test
    void resetPassword_InvalidToken() {
        String resetToken = "invalidToken";
        when(userRepo.findByResetToken(resetToken)).thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class, () -> passwordResetService.resetPassword(resetToken, "newPassword"));

        verify(userRepo, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void resetPassword_TokenExpired() {
        String resetToken = UUID.randomUUID().toString();
        User mockUser = new User();
        mockUser.setResetToken(resetToken);
        mockUser.setTokenExpiryDate(LocalDateTime.now().minusMinutes(10));

        when(userRepo.findByResetToken(resetToken)).thenReturn(Optional.of(mockUser));

        assertThrows(InvalidTokenException.class, () -> passwordResetService.resetPassword(resetToken, "newPassword"));

        verify(userRepo, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
