package org.example.sbappwithprofilesandconfigurations.Security;


import org.example.sbappwithprofilesandconfigurations.Controller.PasswordResetController;
import org.example.sbappwithprofilesandconfigurations.Controller.ProductController;
import org.example.sbappwithprofilesandconfigurations.Model.Role;
import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Service.ActivityLogService;
import org.example.sbappwithprofilesandconfigurations.Service.RegistrationService;
import org.example.sbappwithprofilesandconfigurations.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@Import(SecurityConfig.class)
public class JwtAuthenticationFilterTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private UserService userService;
    @MockBean
    private ActivityLogService activityLogService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private PasswordResetController passwordResetController;
    @MockBean
    private ProductController productController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAccessWithValidToken() throws Exception {
        String validToken = "Token";
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(validToken)).thenReturn("testUser");


        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setRoles(Set.of(new Role(RoleName.SUPER_ADMIN)));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                        .collect(Collectors.toSet())
        );

        when(userService.loadUserByUsername("testUser")).thenReturn(userDetails);

        mockMvc.perform(get("/api/admin/userList")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }


    @Test
    void testAccessWithInvalidToken() throws Exception {
        String invalidToken = "invalidToken";

        when(jwtTokenProvider.validateToken(invalidToken)).thenReturn(false);
        mockMvc.perform(get("/api/admin/userList")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isForbidden());
    }
}
