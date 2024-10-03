package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Model.Role;
import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Repo.RoleRepo;
import org.example.sbappwithprofilesandconfigurations.Repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    public RegistrationService(UserRepo userRepo, PasswordEncoder passwordEncoder, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    public User registerUser(String username, String password, Set<RoleName> roleNames) {
        Set<Role> roles = new HashSet<>();

        for (RoleName roleName : roleNames) {
            Role role = roleRepo.findRoleByRoleName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
            roles.add(role);
        }
        if (userRepo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, roles);
        return userRepo.save(user);
    }
}
