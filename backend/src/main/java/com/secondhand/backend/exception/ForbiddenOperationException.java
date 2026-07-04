package com.secondhand.backend.exception;

/**
 * Thrown when a user is not allowed to perform the requested operation
 * (e.g. editing someone else's listing, or a regular user accessing the admin panel).
 */

public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
