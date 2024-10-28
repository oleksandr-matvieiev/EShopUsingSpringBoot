package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Exception.RoleNotFoundException;
import org.example.sbappwithprofilesandconfigurations.Exception.UsernameAlreadyExistsException;
import org.example.sbappwithprofilesandconfigurations.Model.Role;
import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Repo.RoleRepo;
import org.example.sbappwithprofilesandconfigurations.Repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    public RegistrationService(UserRepo userRepo, PasswordEncoder passwordEncoder, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    public User registerUser(String username, String email, String password, Set<RoleName> roleNames) {
        logger.info("Registering new user with username: {}", username);


        Set<Role> roles = new HashSet<>();
        for (RoleName roleName : roleNames) {
            Role role = roleRepo.findRoleByRoleName(roleName)
                    .orElseThrow(() -> {
                        logger.error("Role not found: {}", roleName);
                        return new RoleNotFoundException("Role not found: " + roleName);
                    });
            roles.add(role);
        }
        if (userRepo.findByUsername(username).isPresent()) {
            logger.warn("Username already exists: {}", username);
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, encodedPassword);
        user.setRoles(roles);
        logger.info("User {} registered successfully", username);
        return userRepo.save(user);
    }
}
