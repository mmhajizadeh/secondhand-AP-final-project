package com.secondhand.backend.exception;

/**
 * Thrown when the requested entity (user, listing, etc) cannot be found
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
