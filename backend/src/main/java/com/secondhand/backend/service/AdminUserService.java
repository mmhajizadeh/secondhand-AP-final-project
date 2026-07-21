package com.secondhand.backend.service;

import com.secondhand.backend.dto.AdminStatsResponse;
import com.secondhand.backend.dto.UserSummaryResponse;
import com.secondhand.backend.entity.User;
import com.secondhand.backend.entity.UserStatus;
import com.secondhand.backend.exception.ResourceNotFoundException;
import com.secondhand.backend.repository.ConversationRepository;
import com.secondhand.backend.repository.RatingRepository;
import com.secondhand.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Admin-only user management: listing users, blocking/unblocking them, and generating stats.
 * Per the spec a blocked user should not be able to log in or perform
 * actions (already enforced by User.isEnabled()/isAccountNonLocked() in
 * Spring Security, since those check UserStatus).
 */
@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final RatingRepository ratingRepository;

    public AdminUserService(UserRepository userRepository,
                            ConversationRepository conversationRepository,
                            RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.ratingRepository = ratingRepository;
    }

    public List<UserSummaryResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toSummary)
                .toList();
    }

    public UserSummaryResponse blockUser(Long userId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getUsername().equalsIgnoreCase(currentUsername)) {
            throw new IllegalArgumentException("You cannot block your own admin account!");
        }

        user.setStatus(UserStatus.BLOCKED);
        return toSummary(userRepository.save(user));
    }

    public UserSummaryResponse unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(UserStatus.ACTIVE);
        return toSummary(userRepository.save(user));
    }

    public AdminStatsResponse getAdminStats() {
        List<User> allUsers = userRepository.findAll();
        long totalUsers = allUsers.size();
        long activeUsers = allUsers.stream().filter(u -> u.getStatus() == UserStatus.ACTIVE).count();
        long blockedUsers = allUsers.stream().filter(u -> u.getStatus() == UserStatus.BLOCKED).count();
        long totalConversations = conversationRepository.count();
        long totalRatings = ratingRepository.count();

        return new AdminStatsResponse(totalUsers, activeUsers, blockedUsers, totalConversations, totalRatings);
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
