package org.example.sbappwithprofilesandconfigurations.Security;

import org.example.sbappwithprofilesandconfigurations.Security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {
    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
    }

    @Test
    void testGenerateToken_ValidAuthentication() {
        UserDetails user = new User("testUser", "password", new ArrayList<>());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

        String token = jwtTokenProvider.generateToken(authentication);
        assertNotNull(token);
    }

    @Test
    void testGetUsernameFromJWT_ValidToken() {
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        String token = jwtTokenProvider.generateToken(authentication);
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalid jwt token";
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertFalse(isValid);
    }
}
