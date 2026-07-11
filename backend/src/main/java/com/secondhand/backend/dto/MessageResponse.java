package com.secondhand.backend.dto;

import java.time.LocalDateTime;

/**
 * Response representing a single message.
 */
public class MessageResponse {

    private Long id;
    private Long conversationId;
    private String senderUsername;
    private String content;
    private LocalDateTime sentAt;

    public MessageResponse(Long id, Long conversationId, String senderUsername, String content, LocalDateTime sentAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
