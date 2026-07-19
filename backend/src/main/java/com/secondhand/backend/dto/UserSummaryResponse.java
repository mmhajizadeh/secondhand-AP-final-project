package com.secondhand.backend.dto;

/**
 * Summary view of a user, returned to the admin panel.
 * Deliberately excludes the password hash and other sensitive fields.
 */
public class UserSummaryResponse {

    private Long id;
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String role;
    private String status;

    public UserSummaryResponse(Long id, String username, String fullName, String phone,
                                String email, String role, String status) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getFullName() {
        return fullName;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public String getRole() {
        return role;
    }
    public String getStatus() {
        return status;
    }
}
