package com.secondhand.backend.dto;

import java.time.LocalDateTime;

/**
 * Response returned after submitting a rating, or when listing individual ratings.
 */
public class RatingResponse {

    private Long id;
    private Long raterId;
    private String raterUsername;
    private Long sellerId;
    private Long advertisementId;
    private int score;
    private String comment;
    private LocalDateTime createdAt;

    public RatingResponse(Long id, Long raterId, String raterUsername, Long sellerId,
                           Long advertisementId, int score, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.raterId = raterId;
        this.raterUsername = raterUsername;
        this.sellerId = sellerId;
        this.advertisementId = advertisementId;
        this.score = score;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getRaterId() {
        return raterId;
    }

    public String getRaterUsername() {
        return raterUsername;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
