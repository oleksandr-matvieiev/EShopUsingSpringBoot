package org.example.sbappwithprofilesandconfigurations.Controller;


import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserService service;

    public AdminController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestParam String username, @RequestParam RoleName role) {
        service.assignRole(username, role);
        try {
            service.assignRole(username, role);
            logger.info("Role {} assigned to user {}", role, username);
            return ResponseEntity.ok("Role " + role + " assigned to user " + username);
        } catch (Exception e) {
            logger.error("Failed to assign role {} to user {}: {}", role, username, e.getMessage());
            return ResponseEntity.status(500).body("Failed to assign role");
        }
    }
}
