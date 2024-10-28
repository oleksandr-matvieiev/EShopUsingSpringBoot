package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Exception.RoleNotFoundException;
import org.example.sbappwithprofilesandconfigurations.Exception.UsernameAlreadyExistsException;
import org.example.sbappwithprofilesandconfigurations.Model.Role;
import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Repo.RoleRepo;
import org.example.sbappwithprofilesandconfigurations.Repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepo roleRepo;
    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        String username = "testUsername";
        String email = "test@example.com";
        String password = "password";
        Set<RoleName> roleNames = Set.of(RoleName.USER);
        Role role = new Role(RoleName.USER);

        when(roleRepo.findRoleByRoleName(RoleName.USER)).thenReturn(Optional.of(role));

        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        User savedUser = new User(username, email, encodedPassword);
        savedUser.setRoles(Set.of(role));
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        User result = registrationService.registerUser(username, email, password, roleNames);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
        assertTrue(result.getRoles().contains(role));

        verify(roleRepo, times(1)).findRoleByRoleName(RoleName.USER);
        verify(userRepo, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepo, times(1)).save(any(User.class));

    }

    @Test
    void registerUser_UsernameAlreadyExists() {
        String username = "existingUser";
        String email = "existingUser@example.com";
        String password = "password123";
        Set<RoleName> roleNames = Set.of(RoleName.USER);

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(new User()));
        when(roleRepo.findRoleByRoleName(RoleName.USER)).thenReturn(Optional.of(new Role(RoleName.USER)));
        assertThrows(UsernameAlreadyExistsException.class, () -> registrationService.registerUser(username, email, password, roleNames));

        verify(userRepo, times(1)).findByUsername(username);
        verify(roleRepo, times(1)).findRoleByRoleName(RoleName.USER);
        verify(passwordEncoder, never()).encode(any());
        verify(userRepo, never()).save(any(User.class));

    }
    @Test
    void registerUser_RoleNotFound() {
        String username = "test";
        String email = "test@example.com";
        String password = "password";
        Set<RoleName> roleNames = Set.of(RoleName.ADMIN);

        when(roleRepo.findRoleByRoleName(RoleName.ADMIN)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class,
                () -> registrationService.registerUser(username, email, password, roleNames));

        verify(roleRepo, times(1)).findRoleByRoleName(RoleName.ADMIN);
        verify(userRepo, never()).findByUsername(username);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any(User.class));
    }

}
