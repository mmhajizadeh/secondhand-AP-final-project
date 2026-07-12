package com.secondhand.frontend.service.dto;

/**
 * Mirrors the backends StartConversationRequest DTO
 */
public class StartConversationRequest {
    private Long advertisementId;
    private String sellerUsername;
    private String content;

    public StartConversationRequest() {
    }
    public StartConversationRequest(Long advertisementId, String sellerUsername, String content) {
        this.advertisementId = advertisementId;
        this.sellerUsername = sellerUsername;
        this.content = content;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
