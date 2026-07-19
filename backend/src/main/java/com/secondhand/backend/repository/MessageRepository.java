package com.secondhand.backend.repository;

import com.secondhand.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

    // Used to show a preview of the last message in the conversation list
    Optional<Message> findFirstByConversationIdOrderBySentAtDesc(Long conversationId);
}
