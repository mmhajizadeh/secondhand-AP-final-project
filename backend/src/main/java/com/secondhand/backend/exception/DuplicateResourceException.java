package com.secondhand.backend.exception;

/**
 * Thrown when a user tries to register data that must be unique
 * (e.g. a duplicate username or phone number) but it already exists.
 */

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
