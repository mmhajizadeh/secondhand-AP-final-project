package com.secondhand.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * A rating a buyer gives to a seller, tied to a specific advertisement.
 * Per the spec: score must be 1-5, one rating per (rater, advertisement) pair,
 * and users cannot rate themselves.
 *
 * Note: advertisementId is a plain foreign-key-style field (not a JPA
 * @ManyToOne relationship) because the Advertisement entity lives on a
 * separate feature branch. This will be wired up to a real relationship
 * during integration
 */
@Entity
@Table(name = "ratings", uniqueConstraints = {
        // A given rater can only rate the same advertisement once
        @UniqueConstraint(columnNames = {"rater_id", "advertisement_id"})
})
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rater_id", nullable = false)
    private Long raterId; // the buyer giving the rating

    @Column(name = "seller_id", nullable = false)
    private Long sellerId; // the user being rated

    @Column(name = "advertisement_id", nullable = false)
    private Long advertisementId;

    @Column(nullable = false)
    private int score; // 1 to 5

    @Column(length = 500)
    private String comment; // optional

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Rating() {
    }

    public Rating(Long raterId, Long sellerId, Long advertisementId, int score, String comment) {
        this.raterId = raterId;
        this.sellerId = sellerId;
        this.advertisementId = advertisementId;
        this.score = score;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
