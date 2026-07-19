package com.secondhand.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Input payload to start a conversation about an advertisement (or send a
 * message if the conversation already exists between this buyer and seller).
 */
public class StartConversationRequest {

    @NotNull(message = "Advertisement id is required")
    private Long advertisementId;

    @NotBlank(message = "Seller username is required")
    private String sellerUsername;

    @NotBlank(message = "Message content cannot be empty")
    private String content;

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
