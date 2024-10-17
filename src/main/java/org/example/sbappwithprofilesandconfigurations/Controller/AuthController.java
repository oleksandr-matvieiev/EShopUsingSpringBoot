package org.example.sbappwithprofilesandconfigurations.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Security.JwtTokenProvider;
import org.example.sbappwithprofilesandconfigurations.Service.ActivityLogService;
import org.example.sbappwithprofilesandconfigurations.Service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final RegistrationService registrationService;
    private final ActivityLogService logService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, RegistrationService registrationService, ActivityLogService logService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.registrationService = registrationService;
        this.logService = logService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user, HttpServletRequest request) {
        logger.info("Registering user with username: {}", user.getUsername());
        String ipAddress = request.getRemoteAddr();
        try {
            User registeredUser = registrationService.registerUser(user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    Set.of(RoleName.USER));

            if (registeredUser.getUsername() != null) {
                logger.info("User {} registered successfully", user.getUsername());
                logService.logActivity(user.getUsername(), "User registered successfully", ipAddress);
                return ResponseEntity.ok("User registered successfully");
            } else {
                logger.warn("User registration failed for {}", user.getUsername());
                logService.logActivity(user.getUsername(), "User registration failed for user: " + user.getUsername(), ipAddress);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
            }
        } catch (Exception e) {
            logger.error("Error during user registration for {}: {}", user.getUsername(), e.getMessage());
            logService.logActivity(user.getUsername(), "Error during registration of user: " + user.getUsername(), ipAddress);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    @PostMapping("/login")
    public Map<String, String> authenticateUser(@RequestBody Map<String, String> loginRequest, HttpServletRequest request) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        String ipAddress = request.getRemoteAddr();
        logService.logActivity(username, "User logged in", ipAddress);
        return Map.of("Token", token, "roles", roles);
    }


}
