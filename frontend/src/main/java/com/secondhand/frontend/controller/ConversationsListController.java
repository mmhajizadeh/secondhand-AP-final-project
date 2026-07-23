package com.secondhand.frontend.controller;

import com.secondhand.frontend.service.ApiException;
import com.secondhand.frontend.service.ChatService;
import com.secondhand.frontend.service.dto.ConversationResponse;
import com.secondhand.frontend.service.dto.StartConversationRequest;
import com.secondhand.frontend.session.SessionManager;
import com.secondhand.frontend.util.NavigationContext;
import com.secondhand.frontend.util.SceneManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.List;

public class ConversationsListController {

    @FXML
    private ListView<ConversationResponse> conversationsListView;

    private final ChatService chatService = new ChatService();

    @FXML
    public void initialize() {
        conversationsListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(ConversationResponse item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String me = SessionManager.getInstance().getCurrentUser().getUsername();
                    String otherParty = me.equals(item.getBuyerUsername())
                            ? item.getSellerUsername()
                            : item.getBuyerUsername();

                    String preview = (item.getLastMessagePreview() != null && !item.getLastMessagePreview().isBlank())
                            ? item.getLastMessagePreview()
                            : "(no messages yet)";

                    String adTitle = (item.getAdvertisementTitle() != null && !item.getAdvertisementTitle().isBlank())
                            ? item.getAdvertisementTitle()
                            : "Ad #" + item.getAdvertisementId();

                    setText("Chat with " + otherParty + " [" + adTitle + "] — " + preview);
                }
            }
        });

        conversationsListView.setOnMouseClicked(event -> {
            ConversationResponse selected = conversationsListView.getSelectionModel().getSelectedItem();
            if (selected != null && event.getClickCount() == 2) {
                openConversation(selected);
            }
        });

        // Read target information from context
        Long targetAdId = NavigationContext.getTargetAdvertisementId();
        String targetSeller = NavigationContext.getTargetSellerUsername();

        loadConversations(targetAdId, targetSeller);
    }

    @FXML
    private void handleRefresh() {
        loadConversations(null, null);
    }

    @FXML
    private void handleBack() {
        SceneManager.switchTo("/com/secondhand/frontend/view/main-view.fxml", "Second-Hand Marketplace");
    }

    private void loadConversations(Long targetAdId, String targetSeller) {
        Task<List<ConversationResponse>> task = new Task<>() {
            @Override
            protected List<ConversationResponse> call() throws Exception {
                return chatService.getMyConversations();
            }
        };

        task.setOnSucceeded(event -> {
            List<ConversationResponse> conversations = task.getValue();

            if (conversations != null) {
                conversationsListView.getItems().setAll(conversations);
            }

            if (targetAdId != null && targetAdId > 0) {
                String me = SessionManager.getInstance().getCurrentUser().getUsername();

                if (me.equals(targetSeller)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING,
                            "You cannot start a conversation about your own advertisement.");
                    alert.showAndWait();
                    return;
                }

                ConversationResponse existingChat = findByAdId(conversations, targetAdId);

                if (existingChat != null) {
                    openConversation(existingChat);
                } else if (targetSeller != null && !targetSeller.isBlank()) {
                    createConversationThenOpen(targetAdId, targetSeller);
                }
            }
        });

        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            String message = ex instanceof ApiException apiEx
                    ? apiEx.getMessage()
                    : "Could not connect to the server. Make sure the backend is running.";
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.showAndWait();
        });

        new Thread(task).start();
    }

    private ConversationResponse findByAdId(List<ConversationResponse> conversations, Long adId) {
        if (conversations == null) {
            return null;
        }
        String me = SessionManager.getInstance().getCurrentUser().getUsername();
        for (ConversationResponse c : conversations) {
            Long cAdId = c.getAdvertisementId();
            boolean adMatches = cAdId != null && cAdId > 0 && cAdId.equals(adId);
            boolean iAmParticipant = me.equals(c.getBuyerUsername()) || me.equals(c.getSellerUsername());
            if (adMatches && iAmParticipant) {
                return c;
            }
        }
        return null;
    }

    private void createConversationThenOpen(Long adId, String seller) {
        Task<ConversationResponse> task = new Task<>() {
            @Override
            protected ConversationResponse call() throws Exception {
                StartConversationRequest request =
                        new StartConversationRequest(adId, seller, "Hi, I'm interested in your advertisement.");
                chatService.startConversation(request);

                List<ConversationResponse> refreshed = chatService.getMyConversations();
                ConversationResponse created = findByAdId(refreshed, adId);

                if (created == null) {
                    throw new IllegalStateException(
                            "Conversation for ad #" + adId + " was created but not found in refreshed list.");
                }
                return created;
            }
        };

        task.setOnSucceeded(e -> {
            ConversationResponse created = task.getValue();
            conversationsListView.getItems().add(created);
            openConversation(created);
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            Platform.runLater(() -> {
                String message = ex instanceof ApiException apiEx
                        ? apiEx.getMessage()
                        : "Could not start conversation automatically.";
                Alert alert = new Alert(Alert.AlertType.ERROR, message);
                alert.showAndWait();
            });
        });

        new Thread(task).start();
    }

    private void openConversation(ConversationResponse conversation) {
        String me = SessionManager.getInstance().getCurrentUser().getUsername();
        String otherParty = me.equals(conversation.getBuyerUsername())
                ? conversation.getSellerUsername()
                : conversation.getBuyerUsername();

        NavigationContext.setCurrentConversation(conversation.getId(), "Chat with " + otherParty);

        if (conversation.getSellerId() != null) {
            NavigationContext.setRatingSellerId(conversation.getSellerId());
        } else if (NavigationContext.getTargetSellerId() != null) {
            NavigationContext.setRatingSellerId(NavigationContext.getTargetSellerId());
        }

        if (conversation.getAdvertisementId() != null) {
            NavigationContext.setRatingAdvertisementId(conversation.getAdvertisementId());
        } else if (NavigationContext.getTargetAdvertisementId() != null) {
            NavigationContext.setRatingAdvertisementId(NavigationContext.getTargetAdvertisementId());
        }

        if (conversation.getSellerUsername() != null) {
            NavigationContext.setRatingSellerUsername(conversation.getSellerUsername());
        } else if (NavigationContext.getTargetSellerUsername() != null) {
            NavigationContext.setRatingSellerUsername(NavigationContext.getTargetSellerUsername());
        }

        if (conversation.getAdvertisementTitle() != null) {
            NavigationContext.setRatingAdvertisementTitle(conversation.getAdvertisementTitle());
        } else if (NavigationContext.getTargetAdvertisementTitle() != null) {
            NavigationContext.setRatingAdvertisementTitle(NavigationContext.getTargetAdvertisementTitle());
        }

        NavigationContext.setTargetAdvertisementId(null);
        NavigationContext.setTargetSellerId(null);
        NavigationContext.setTargetSellerUsername(null);
        NavigationContext.setTargetAdvertisementTitle(null);

        SceneManager.switchTo("/com/secondhand/frontend/view/conversation-detail-view.fxml", "Conversation");
    }

    }