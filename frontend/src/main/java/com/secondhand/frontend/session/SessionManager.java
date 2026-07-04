package com.secondhand.frontend.session;

import com.secondhand.frontend.model.CurrentUser;

/**
 * Manages the JWT token and logged-in user session in memory.
 * <p>
 * Implemented as a singleton so every screen (login, listings, chat, admin panel)
 * can access the same session data without manual parameter passing.
 * </p>
 * <p>
 * The token exists only in memory. If the application is closed,
 * the user must log in again
 * (no "remember me" or persistent session required).
 * </p>
 */
public class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();

    private String token;
    private CurrentUser currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public void startSession(String token, CurrentUser user) {
        this.token = token;
        this.currentUser = user;
    }

    public void clearSession() {
        this.token = null;
        this.currentUser = null;
    }
    public boolean isLoggedIn() {
        return token != null && currentUser != null;
    }

    public String getToken() {
        return token;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public boolean isAdmin() {
        return isLoggedIn() && currentUser.isAdmin();
    }
}
