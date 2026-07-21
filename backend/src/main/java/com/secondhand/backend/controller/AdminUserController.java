package com.secondhand.backend.controller;

import com.secondhand.backend.dto.AdminStatsResponse;
import com.secondhand.backend.dto.UserSummaryResponse;
import com.secondhand.backend.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin-only endpoints for managing users and statistics.
 * Restricted to ADMIN role via SecurityConfig's "/api/admin/**" rule.
 */
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getAdminStats() {
        return ResponseEntity.ok(adminUserService.getAdminStats());
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<UserSummaryResponse> blockUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.blockUser(id));
    }

    @PatchMapping("/{id}/unblock")
    public ResponseEntity<UserSummaryResponse> unblockUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.unblockUser(id));
    }
}
