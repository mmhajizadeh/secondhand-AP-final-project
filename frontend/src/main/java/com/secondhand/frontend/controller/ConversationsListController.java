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

        // Read + consume the navigation target ONCE, right here, before anything
        // async happens. This avoids the target being read twice or read after
        // it was already cleared by a previous run.
        Long targetAdId = NavigationContext.getTargetAdvertisementId();
        String targetSeller = NavigationContext.getTargetSellerUsername();
        NavigationContext.setTargetAdvertisementId(null);
        NavigationContext.setTargetSellerUsername(null);

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

    /**
     * Single entry point for loading the conversation list.
     * If targetAdId/targetSeller are non-null, resolves them (find-or-create)
     * after the list loads. Otherwise just displays the list.
     */
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
        for (ConversationResponse c : conversations) {
            Long cAdId = c.getAdvertisementId();
            if (cAdId != null && cAdId > 0 && cAdId.equals(adId)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Creates a new conversation on a background thread, then re-fetches the
     * conversation list on the SAME background thread (no nested Task), and
     * opens the new conversation on the FX thread once everything is done.
     */
    private void createConversationThenOpen(Long adId, String seller) {
        Task<ConversationResponse> task = new Task<>() {
            @Override
            protected ConversationResponse call() throws Exception {
                StartConversationRequest request =
                        new StartConversationRequest(adId, seller, "Hi, I'm interested in your advertisement.");
                chatService.startConversation(request);

                // Re-fetch on this same background thread — no nested Task.
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
        SceneManager.switchTo("/com/secondhand/frontend/view/conversation-detail-view.fxml", "Conversation");
    }
}