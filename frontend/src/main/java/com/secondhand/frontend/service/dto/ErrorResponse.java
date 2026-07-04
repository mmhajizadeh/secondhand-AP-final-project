package com.secondhand.frontend.service.dto;

/**
 * Mirrors the backend's standard error format: { "message": "...", "status": ... }
 */
public class ErrorResponse {

    private String message;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
