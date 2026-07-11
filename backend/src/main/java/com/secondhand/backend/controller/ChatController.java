package com.secondhand.backend.controller;

import com.secondhand.backend.dto.*;
import com.secondhand.backend.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides communication and chat-related endpoints.
 * <p>
 * All endpoints within this class are secured and require a valid authenticated session,
 * as globally enforced by the {@code SecurityConfig}.
 * </p>
 */
@RestController
@RequestMapping("/api/conversations")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Starts a new conversation for the specified advertisement.
     * <p>
     * If a conversation already exists with the same seller for this ad
     * the new message will automatically be appended to that existing thread.
     * </p>
     */
    @PostMapping
    public ResponseEntity<MessageResponse> startConversation(@Valid @RequestBody StartConversationRequest request,
                                                               Authentication authentication) {
        String buyerUsername = authentication.getName();
        return ResponseEntity.ok(chatService.startConversationOrSendMessage(buyerUsername, request));
    }

    /**
     * Lists all conversations where the current user is either the buyer or the seller.
     */
    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getMyConversations(Authentication authentication) {
        return ResponseEntity.ok(chatService.getConversationsForUser(authentication.getName()));
    }

    /**
     * Gets all messages in a conversation, ordered oldest to newest.
     */
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(chatService.getMessages(authentication.getName(), id));
    }

    /**
     * Sends a new message in an existing conversation.
     */
    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> sendMessage(@PathVariable Long id,
                                                         @Valid @RequestBody SendMessageRequest request,
                                                         Authentication authentication) {
        return ResponseEntity.ok(chatService.sendMessage(authentication.getName(), id, request));
    }
}
