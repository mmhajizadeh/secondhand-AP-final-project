package com.secondhand.backend.dto;

import java.util.List;

/**
 * Summary of a seller's ratings: average score, total count, and the
 * individual ratings (most recent first) so the UI can show both an
 * overview and the detailed list.
 */
public class SellerRatingSummary {

    private Long sellerId;
    private double averageScore;
    private long totalRatings;
    private List<RatingResponse> ratings;

    public SellerRatingSummary(Long sellerId, double averageScore, long totalRatings, List<RatingResponse> ratings) {
        this.sellerId = sellerId;
        this.averageScore = averageScore;
        this.totalRatings = totalRatings;
        this.ratings = ratings;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public long getTotalRatings() {
        return totalRatings;
    }

    public List<RatingResponse> getRatings() {
        return ratings;
    }
}
