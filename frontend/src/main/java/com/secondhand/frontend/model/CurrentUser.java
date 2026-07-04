package com.secondhand.frontend.model;

/**
 * Holds the basic info of the currently logged in user on the client side.
 * Mirrors the fields returned by the backends AuthResponse.
 */
public class CurrentUser {

    private final Long id;
    private final String username;
    private final String fullName;
    private final String role;

    public CurrentUser(Long id, String username, String fullName, String role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}
