package org.example.sbappwithprofilesandconfigurations.Controller;


import org.example.sbappwithprofilesandconfigurations.Model.RoleName;
import org.example.sbappwithprofilesandconfigurations.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService service;

    public AdminController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestParam String username, @RequestParam RoleName role) {
        service.assignRole(username, role);
        return ResponseEntity.ok("Role " + role + " assigned to user " + username);
    }
}
