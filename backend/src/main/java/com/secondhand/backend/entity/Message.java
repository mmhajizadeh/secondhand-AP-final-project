package com.secondhand.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Models a single chat message belonging to a specific conversation.
 * <p>
 * Note: Message delivery is persistence-based rather than real-time. Messages
 * are stored in the database and become visible once the counterparty opens
 * the conversation interface.
 * </p>
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id", nullable = false)
    private Long conversationId;

    @Column(name = "sender_username", nullable = false)
    private String senderUsername;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    public Message() {
    }

    public Message(Long conversationId, String senderUsername, String content) {
        this.conversationId = conversationId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
