package com.secondhand.frontend.service.dto;

/**
 * Mirrors the backends UserSummaryResponse DTO
 */
public class UserSummaryResponse {
    private Long id;
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String role;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString used by the ListView cell display as a simple fallback.
    @Override
    public String toString() {
        return username + " (" + fullName + ") — " + role + " — " + status;
    }
}
