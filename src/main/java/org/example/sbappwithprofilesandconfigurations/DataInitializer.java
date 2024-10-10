package org.example.sbappwithprofilesandconfigurations;

import org.example.sbappwithprofilesandconfigurations.Model.Role;
import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Repo.RoleRepo;
import org.example.sbappwithprofilesandconfigurations.Repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;

    public DataInitializer(RoleRepo roleRepo, UserRepo userRepo, PasswordEncoder encoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepo.findRoleByRoleName(RoleName.USER).isEmpty()) {
            roleRepo.save(new Role(RoleName.USER));
        }
        if (roleRepo.findRoleByRoleName(RoleName.ADMIN).isEmpty()) {
            roleRepo.save(new Role(RoleName.ADMIN));
        }
        if (roleRepo.findRoleByRoleName(RoleName.SUPER_ADMIN).isEmpty()) {
            roleRepo.save(new Role(RoleName.SUPER_ADMIN));
        }
        if (userRepo.findByUsername("superadmin").isEmpty()) {
            Role superAdminRole = roleRepo.findRoleByRoleName(RoleName.SUPER_ADMIN).orElseThrow(() -> new RuntimeException("SUPER_ADMIN role not found"));
            User superAdmin = new User("superadmin", "selfimen2454@gmail.com", encoder.encode("superadmin123"));
            superAdmin.setRoles(Set.of(superAdminRole));
            userRepo.save(superAdmin);
            System.out.println("Super admin created with login 'superadmin' and password 'superadmin123'");
        }
    }
}
