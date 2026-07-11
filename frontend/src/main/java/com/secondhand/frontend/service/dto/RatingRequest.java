package com.secondhand.frontend.service.dto;

/**
 * Mirrors the backends RatingRequest DTO
 */
public class RatingRequest {
    private Long sellerId;
    private Long advertisementId;
    private Integer score;
    private String comment;
    public RatingRequest() {
    }

    public RatingRequest(Long sellerId, Long advertisementId, Integer score, String comment) {
        this.sellerId = sellerId;
        this.advertisementId = advertisementId;
        this.score = score;
        this.comment = comment;
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
