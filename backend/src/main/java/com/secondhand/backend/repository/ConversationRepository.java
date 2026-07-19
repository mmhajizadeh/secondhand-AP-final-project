package com.secondhand.backend.repository;

import com.secondhand.backend.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByAdvertisementIdAndBuyerUsername(Long advertisementId, String buyerUsername);

    // All conversations where the user is either the buyer or the seller
    List<Conversation> findByBuyerUsernameOrSellerUsername(String buyerUsername, String sellerUsername);
}
