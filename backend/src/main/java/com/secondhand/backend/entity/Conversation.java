package com.secondhand.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Models a chat conversation between a buyer and a seller for a particular advertisement.
 * <p>
 * A single advertisement can have multiple conversations (one per buyer). However, only a single
 * conversation is permitted for any unique buyer-seller-advertisement combination, enforced by
 * a unique constraint. New messages for the same combination are appended to the existing thread.
 * </p>
 * <p>
 * Note: Decoupled fields ({@code advertisementId}, {@code sellerUsername}, {@code buyerUsername})
 * are used instead of formal JPA relationships to maintain independence from the advertisement
 * module, which identifies ownership strictly via {@code Advertisement.ownerUsername}.
 * </p>
 */
@Entity
@Table(name = "conversations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"advertisement_id", "buyer_username"})
})
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "advertisement_id", nullable = false)
    private Long advertisementId;

    @Column(name = "buyer_username", nullable = false)
    private String buyerUsername;

    @Column(name = "seller_username", nullable = false)
    private String sellerUsername;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Conversation() {
    }

    public Conversation(Long advertisementId, String buyerUsername, String sellerUsername) {
        this.advertisementId = advertisementId;
        this.buyerUsername = buyerUsername;
        this.sellerUsername = sellerUsername;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
