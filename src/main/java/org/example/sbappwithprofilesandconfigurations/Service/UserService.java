package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Exception.RoleNotFoundException;
import org.example.sbappwithprofilesandconfigurations.Exception.UserNotFoundException;
import org.example.sbappwithprofilesandconfigurations.Model.Role;
import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Repo.RoleRepo;
import org.example.sbappwithprofilesandconfigurations.Repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    public UserService(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UserNotFoundException("User not found.");
                });
        logger.info("User {} loaded successfully", username);
        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName())).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Transactional
    public void assignRole(String username, RoleName roleName) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UserNotFoundException("User not found.");
                });
        Role role = roleRepo.findRoleByRoleName(roleName)
                .orElseThrow(() -> {
                    logger.error("Role not found: {}", roleName);
                    return new RoleNotFoundException("Role not found: " + roleName);
                });

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            logger.info("Assigning role {} to user {}", roleName, username);
            userRepo.save(user);
        }
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Transactional
    public Page<User> getUsersWithFilters(String search, String role, Pageable pageable) {
        Role roleEntity = null;

        if (role != null) {
            roleEntity = roleRepo.findRoleByRoleName(RoleName.valueOf(role))
                    .orElseThrow(() -> new RoleNotFoundException("Role not found: " + role));
        }

        if (search != null && roleEntity != null) {
            return userRepo.findByUsernameContainingAndRoles(search, roleEntity, pageable);
        } else if (search != null) {
            return userRepo.findByUsernameContaining(search, pageable);
        } else if (roleEntity != null) {
            return userRepo.findByRoles(roleEntity, pageable);
        } else {
            return userRepo.findAll(pageable);
        }
    }
}
