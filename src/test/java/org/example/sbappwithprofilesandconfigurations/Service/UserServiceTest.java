package org.example.sbappwithprofilesandconfigurations.Service;


import org.example.sbappwithprofilesandconfigurations.Exception.RoleNotFoundException;
import org.example.sbappwithprofilesandconfigurations.Exception.UserNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private RoleRepo roleRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists() {
        User mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setPassword("password");
        Role role = new Role(RoleName.USER);
        mockUser.setRoles(Set.of(role));

        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepo.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername("nonExistentUser"));
    }

    @Test
    void assignRole_Success() {
        User mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setRoles(new HashSet<>());  // Ініціалізуємо пустий HashSet для ролей

        Role role = new Role(RoleName.USER);

        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(roleRepo.findRoleByRoleName(RoleName.USER)).thenReturn(Optional.of(role));

        userService.assignRole("testUser", RoleName.USER);

        verify(userRepo, times(1)).save(mockUser);
        assertTrue(mockUser.getRoles().contains(role));
    }


    @Test
    void assignRole_UserNotFound() {
        when(userRepo.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.assignRole("nonExistentUser", RoleName.USER));

    }

    @Test
    void assignRole_RoleNotFound() {
        User mockUser = new User();
        mockUser.setUsername("testUser");

        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(roleRepo.findRoleByRoleName(RoleName.ADMIN)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.assignRole("testUser", RoleName.ADMIN));
    }

    @Test
    void findUserByUsername_UserExists() {
        User mockUser = new User();
        mockUser.setUsername("testUser");

        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        Optional<User> user = userService.findUserByUsername("testUser");

        assertTrue(user.isPresent());
        assertEquals("testUser", user.get().getUsername());
    }

    @Test
    void findUserByUsername_UserNotFound() {
        when(userRepo.findByUsername("nonExistentUser")).thenReturn(Optional.empty());
        Optional<User> user = userService.findUserByUsername("nonExistentUser");

        assertFalse(user.isPresent());
    }

    @Test
    void getUsersWithFilters_ByUsername() {
        User mockUser = new User();
        mockUser.setUsername("testUser");

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(mockUser), pageable, 1);

        when(userRepo.findByUsernameContaining("test", pageable)).thenReturn(userPage);

        Page<User> result = userService.getUsersWithFilters("test", null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("testUser", result.getContent().get(0).getUsername());
    }

    @Test
    void getUsersWithFilters_ByRole() {
        Role role = new Role(RoleName.USER);
        User mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setRoles(Set.of(role));

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(mockUser), pageable, 1);

        when(roleRepo.findRoleByRoleName(RoleName.USER)).thenReturn(Optional.of(role));
        when(userRepo.findByRoles(role, pageable)).thenReturn(userPage);

        Page<User> result = userService.getUsersWithFilters(null, "USER", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("testUser", result.getContent().get(0).getUsername());
    }

    @Test
    void getUsersWithFilters_ByRoleNotFound() {
        Pageable pageable = PageRequest.of(1, 10);

        when(roleRepo.findRoleByRoleName(RoleName.ADMIN)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.getUsersWithFilters(null, "ADMIN", pageable));
    }

    @Test
    void getUsersWithFilters_ByUsernameAndRole() {
        User mockUser = new User();
        mockUser.setUsername("testUser");
        Role role = new Role(RoleName.USER);
        mockUser.setRoles(Set.of(role));

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(Collections.singletonList(mockUser), pageable, 1);

        when(userRepo.findByUsernameContainingAndRoles("test", role, pageable)).thenReturn(page);
        when(roleRepo.findRoleByRoleName(RoleName.USER)).thenReturn(Optional.of(role));

        Page<User> result = userService.getUsersWithFilters("test", "USER", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("testUser", result.getContent().get(0).getUsername());
    }

}
