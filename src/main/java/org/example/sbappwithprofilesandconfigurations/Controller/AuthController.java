package org.example.sbappwithprofilesandconfigurations.Controller;

import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Security.JwtTokenProvider;
import org.example.sbappwithprofilesandconfigurations.Service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final RegistrationService registrationService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        logger.info("Registering user with username: {}", user.getUsername());

        try {
            User registeredUser = registrationService.registerUser(user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    Set.of(RoleName.USER));

            if (registeredUser.getUsername() != null) {
                logger.info("User {} registered successfully", user.getUsername());
                return ResponseEntity.ok("User registered successfully");
            } else {
                logger.warn("User registration failed for {}", user.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
            }

        } catch (Exception e) {
            logger.error("Error during user registration for {}: {}", user.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    @PostMapping("/login")
    public Map<String, String> authenticateUser(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return Map.of("Token", token);
    }

}
