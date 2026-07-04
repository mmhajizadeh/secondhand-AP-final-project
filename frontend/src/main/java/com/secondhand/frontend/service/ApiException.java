package com.secondhand.frontend.service;

/**
 * Exception thrown when the backend responds with an error.
 * <p>
 * Carries a human-readable message for direct UI display,
 * along with the HTTP status code for conditional handling
 * (e.g., 401 Unauthorized, 409 Conflict, 500 Internal Error).
 * </p>
 */
public class ApiException extends Exception {

    private final int statusCode;

    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
