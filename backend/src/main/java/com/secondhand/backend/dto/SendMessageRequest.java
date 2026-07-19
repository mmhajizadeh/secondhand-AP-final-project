package com.secondhand.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Input payload for sending a message in an existing conversation.
 */
public class SendMessageRequest {

    @NotBlank(message = "Message content cannot be empty")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
