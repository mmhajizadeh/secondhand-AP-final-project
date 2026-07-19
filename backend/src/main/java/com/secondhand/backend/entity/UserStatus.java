package com.secondhand.backend.entity;

/**
 * Represents the possible statuses for a user account.
 * <p>
 * ACTIVE  – The user is fully authorized and can use all system features.
 * BLOCKED – The user has been suspended by an admin, meaning they cannot
 *           log in, post ads, send messages, or perform any other actions.
 * </p>
 */
public enum UserStatus {
    ACTIVE,
    BLOCKED
}
