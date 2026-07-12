package com.secondhand.frontend.service.dto;

/**
 * Mirrors the backends SendMessageRequest DTO
 */
public class SendMessageRequest {
    private String content;

    public SendMessageRequest() {
    }

    public SendMessageRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
