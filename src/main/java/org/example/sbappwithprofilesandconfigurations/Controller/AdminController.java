package org.example.sbappwithprofilesandconfigurations.Controller;


import jakarta.servlet.http.HttpServletRequest;
import org.example.sbappwithprofilesandconfigurations.Model.ActivityLog;
import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Model.User;
import org.example.sbappwithprofilesandconfigurations.Service.ActivityLogService;
import org.example.sbappwithprofilesandconfigurations.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final ActivityLogService logService;

    private final UserService userService;

    public AdminController(ActivityLogService logService, UserService userService) {
        this.logService = logService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestParam String username, @RequestParam RoleName role, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        try {
            userService.assignRole(username, role);
            logger.info("Role {} assigned to user {}", role, username);
            logService.logActivity(username, "Role: " + role + " assigned to user", ipAddress);
            return ResponseEntity.ok("Role " + role + " assigned to user " + username);
        } catch (Exception e) {
            logger.error("Failed to assign role {} to user {}: {}", role, username, e.getMessage());
            logService.logActivity(username, "Failed to assign role: " + role + " to user.", ipAddress);
            return ResponseEntity.status(500).body("Failed to assign role");
        }
    }

    @GetMapping("/userList")
    public ResponseEntity<Page<User>> getALlUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<User> users = userService.getUsersWithFilters(search, role, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("activity-log")
    public ResponseEntity<List<ActivityLog>> getActivityLog() {
        List<ActivityLog> logs = logService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}
