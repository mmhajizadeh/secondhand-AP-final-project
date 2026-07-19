package com.secondhand.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Input payload for submitting a rating for a seller.
 */
public class RatingRequest {

    @NotNull(message = "Seller id is required")
    private Long sellerId;

    @NotNull(message = "Advertisement id is required")
    private Long advertisementId;

    @NotNull(message = "Score is required")
    @Min(value = 1, message = "Score must be between 1 and 5")
    @Max(value = 5, message = "Score must be between 1 and 5")
    private Integer score;

    @Size(max = 500, message = "Comment must be at most 500 characters")
    private String comment;

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
