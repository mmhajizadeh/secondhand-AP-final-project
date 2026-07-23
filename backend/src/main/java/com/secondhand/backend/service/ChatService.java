package com.secondhand.backend.service;

import com.secondhand.backend.dto.*;
import com.secondhand.backend.entity.Advertisement;
import com.secondhand.backend.entity.Conversation;
import com.secondhand.backend.entity.Message;
import com.secondhand.backend.entity.User;
import com.secondhand.backend.exception.ForbiddenOperationException;
import com.secondhand.backend.exception.ResourceNotFoundException;
import com.secondhand.backend.repository.AdvertisementRepository;
import com.secondhand.backend.repository.ConversationRepository;
import com.secondhand.backend.repository.MessageRepository;
import com.secondhand.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Core chat logic.
 * Per the spec:
 * - A buyer cannot start a conversation about their own advertisement.
 * - If a conversation already exists for the same buyer+seller+advertisement,
 *   new messages are appended to it instead of creating a duplicate.
 * - Blocked users cannot send new messages.
 * - Real-time delivery is not required; messages are just stored and shown
 *   whenever the conversation screen is opened.
 */
@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    public ChatService(ConversationRepository conversationRepository,
                       MessageRepository messageRepository,
                       UserRepository userRepository,
                       AdvertisementRepository advertisementRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
    }

    /**
     * Starts a new conversation, or appends the message to an existing one
     * if the buyer already has a conversation with this seller about this ad.
     */
    public MessageResponse startConversationOrSendMessage(String buyerUsername, StartConversationRequest request) {
        if (buyerUsername.equals(request.getSellerUsername())) {
            throw new ForbiddenOperationException("You cannot start a conversation about your own advertisement");
        }

        User buyer = requireActiveUser(buyerUsername);
        requireActiveUser(request.getSellerUsername());

        Conversation conversation = conversationRepository
                .findByAdvertisementIdAndBuyerUsername(request.getAdvertisementId(), buyerUsername)
                .orElseGet(() -> conversationRepository.save(
                        new Conversation(request.getAdvertisementId(), buyerUsername, request.getSellerUsername())
                ));

        Message message = new Message(conversation.getId(), buyer.getUsername(), request.getContent());
        Message saved = messageRepository.save(message);

        return toMessageResponse(saved);
    }

    /**
     * Sends a message in an existing conversation. The sender must be either
     * the buyer or the seller of that conversation. Both participants must
     * be active — if either side has been blocked since the conversation
     * started, no new messages can be sent (matches the rule enforced when
     * starting a new conversation).
     */
    public MessageResponse sendMessage(String senderUsername, Long conversationId, SendMessageRequest request) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        if (!senderUsername.equals(conversation.getBuyerUsername())
                && !senderUsername.equals(conversation.getSellerUsername())) {
            throw new ForbiddenOperationException("You are not a participant in this conversation");
        }

        requireActiveUser(conversation.getBuyerUsername());
        requireActiveUser(conversation.getSellerUsername());

        Message message = new Message(conversationId, senderUsername, request.getContent());
        Message saved = messageRepository.save(message);

        return toMessageResponse(saved);
    }

    public List<MessageResponse> getMessages(String requesterUsername, Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        if (!requesterUsername.equals(conversation.getBuyerUsername())
                && !requesterUsername.equals(conversation.getSellerUsername())) {
            throw new ForbiddenOperationException("You are not a participant in this conversation");
        }

        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId).stream()
                .map(this::toMessageResponse)
                .toList();
    }

    public List<ConversationResponse> getConversationsForUser(String username) {
        List<Conversation> conversations = conversationRepository
                .findByBuyerUsernameOrSellerUsername(username, username);

        return conversations.stream()
                .map(this::toConversationResponse)
                .toList();
    }

    private User requireActiveUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        if (user.getStatus() == com.secondhand.backend.entity.UserStatus.BLOCKED) {
            throw new ForbiddenOperationException("This user is blocked and cannot participate in chat");
        }
        return user;
    }

    private ConversationResponse toConversationResponse(Conversation conversation) {
        Optional<Message> lastMessage = messageRepository
                .findFirstByConversationIdOrderBySentAtDesc(conversation.getId());

        String advertisementTitle = advertisementRepository.findById(conversation.getAdvertisementId())
                .map(Advertisement::getTitle)
                .orElse("Deleted advertisement");

        return new ConversationResponse(
                conversation.getId(),
                conversation.getAdvertisementId(),
                advertisementTitle,
                conversation.getBuyerUsername(),
                conversation.getSellerUsername(),
                conversation.getCreatedAt(),
                lastMessage.map(Message::getContent).orElse(null),
                lastMessage.map(Message::getSentAt).orElse(null)
        );
    }

    private MessageResponse toMessageResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getConversationId(),
                message.getSenderUsername(),
                message.getContent(),
                message.getSentAt()
        );
    }
}