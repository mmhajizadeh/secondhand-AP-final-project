package com.secondhand.backend.dto;

import java.time.LocalDateTime;

/**
 * Data transfer object representing a conversation.
 * <p>
 * Includes the most recent message preview to facilitate efficient rendering of the
 * conversation list screen, avoiding secondary network requests.
 * </p>
 */
public class ConversationResponse {

    private Long id;
    private Long advertisementId;
    private String buyerUsername;
    private String sellerUsername;
    private LocalDateTime createdAt;
    private String lastMessagePreview;
    private LocalDateTime lastMessageAt;

    public ConversationResponse(Long id, Long advertisementId, String buyerUsername, String sellerUsername,
                                 LocalDateTime createdAt, String lastMessagePreview, LocalDateTime lastMessageAt) {
        this.id = id;
        this.advertisementId = advertisementId;
        this.buyerUsername = buyerUsername;
        this.sellerUsername = sellerUsername;
        this.createdAt = createdAt;
        this.lastMessagePreview = lastMessagePreview;
        this.lastMessageAt = lastMessageAt;
    }

    public Long getId() {
        return id;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getLastMessagePreview() {
        return lastMessagePreview;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }
}
