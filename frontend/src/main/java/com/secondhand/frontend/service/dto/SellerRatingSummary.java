package com.secondhand.frontend.service.dto;

import java.util.List;

/**
 * Mirrors the backends SellerRatingSummary DTO
 */
public class SellerRatingSummary {
    private Long sellerId;
    private double averageScore;
    private long totalRatings;
    private List<RatingResponse> ratings;

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(long totalRatings) {
        this.totalRatings = totalRatings;
    }

    public List<RatingResponse> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingResponse> ratings) {
        this.ratings = ratings;
    }
}
