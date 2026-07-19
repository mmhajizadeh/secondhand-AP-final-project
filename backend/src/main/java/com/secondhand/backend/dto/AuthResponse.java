package com.secondhand.backend.dto;

/**
 * Response DTO for successful registration or login.
 * <p>
 * Contains the JWT token along with basic user information.
 * This allows the Frontend to determine the user's role right away
 * and display the appropriate interface (user or admin panel).
 * </p>
 */
public class AuthResponse {

    private String token;
    private Long userId;
    private String username;
    private String fullName;
    private String role;

    public AuthResponse(String token, Long userId, String username, String fullName, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }
}
