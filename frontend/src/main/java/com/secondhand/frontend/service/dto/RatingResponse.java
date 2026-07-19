package com.secondhand.frontend.service.dto;

/**
 * Mirrors the backends RatingResponse DTO
 */
public class RatingResponse {
    private Long id;
    private Long raterId;
    private String raterUsername;
    private Long sellerId;
    private Long advertisementId;
    private int score;
    private String comment;
    private String createdAt;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRaterId() {
        return raterId;
    }

    public void setRaterId(Long raterId) {
        this.raterId = raterId;
    }

    public String getRaterUsername() {
        return raterUsername;
    }

    public void setRaterUsername(String raterUsername) {
        this.raterUsername = raterUsername;
    }

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
