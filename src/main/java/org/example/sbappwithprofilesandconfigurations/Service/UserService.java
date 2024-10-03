package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Model.Role;
import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Repo.RoleRepo;
import org.example.sbappwithprofilesandconfigurations.Repo.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    public UserService(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        Set<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Transactional
    public void assignRole(String username, RoleName roleName) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        Role role = roleRepo.findRoleByRoleName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepo.save(user);
        }
    }
}
