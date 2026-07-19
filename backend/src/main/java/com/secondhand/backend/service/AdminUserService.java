package com.secondhand.backend.service;

import com.secondhand.backend.dto.UserSummaryResponse;
import com.secondhand.backend.entity.User;
import com.secondhand.backend.entity.UserStatus;
import com.secondhand.backend.exception.ResourceNotFoundException;
import com.secondhand.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Admin-only user management: listing users and blocking/unblocking them.
 * Per the spec a blocked user should not be able to log in or perform
 * actions (already enforced by User.isEnabled()/isAccountNonLocked() in
 * Spring Security, since those check UserStatus).
 */
@Service
public class AdminUserService {

    private final UserRepository userRepository;
    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserSummaryResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toSummary)
                .toList();
    }

    public UserSummaryResponse blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(UserStatus.BLOCKED);
        return toSummary(userRepository.save(user));
    }

    public UserSummaryResponse unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(UserStatus.ACTIVE);
        return toSummary(userRepository.save(user));
    }

    private UserSummaryResponse toSummary(User user) {
        return new UserSummaryResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getRole().name(),
                user.getStatus().name()
        );
    }
}
