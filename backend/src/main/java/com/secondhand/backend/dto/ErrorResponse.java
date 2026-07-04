package com.secondhand.backend.dto;

/**
 * Standard error format for all APIs in the system.
 * Per the project spec: { "message": "...", "status": 404 }
 */

public class ErrorResponse {

    private String message;
    private int status;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
