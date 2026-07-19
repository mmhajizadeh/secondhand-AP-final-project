package com.secondhand.frontend.controller;

import com.secondhand.frontend.service.ApiException;
import com.secondhand.frontend.service.ChatService;
import com.secondhand.frontend.service.dto.ConversationResponse;
import com.secondhand.frontend.session.SessionManager;
import com.secondhand.frontend.util.NavigationContext;
import com.secondhand.frontend.util.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.util.List;

public class ConversationsListController {

    @FXML
    private ListView<ConversationResponse> conversationsListView;

    private final ChatService chatService = new ChatService();

    @FXML
    public void initialize() {
        conversationsListView.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
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
                    String preview = item.getLastMessagePreview() != null
                            ? item.getLastMessagePreview()
                            : "(no messages yet)";
                    setText("Ad #" + item.getAdvertisementId() + " — with " + otherParty + " — " + preview);
                }
            }
        });

        conversationsListView.setOnMouseClicked(event -> {
            ConversationResponse selected = conversationsListView.getSelectionModel().getSelectedItem();
            if (selected != null && event.getClickCount() == 2) {
                openConversation(selected);
            }
        });

        loadConversations();
    }

    @FXML
    private void handleRefresh() {
        loadConversations();
    }

    @FXML
    private void handleStartNewConversation() {
        SceneManager.switchTo("/com/secondhand/frontend/view/start-conversation-view.fxml", "Start Conversation");
    }

    @FXML
    private void handleBack() {
        SceneManager.switchTo("/com/secondhand/frontend/view/home-placeholder-view.fxml", "Second-Hand Marketplace");
    }

    private void openConversation(ConversationResponse conversation) {
        String me = SessionManager.getInstance().getCurrentUser().getUsername();
        String otherParty = me.equals(conversation.getBuyerUsername())
                ? conversation.getSellerUsername()
                : conversation.getBuyerUsername();

        NavigationContext.setCurrentConversation(conversation.getId(), "Chat with " + otherParty);
        SceneManager.switchTo("/com/secondhand/frontend/view/conversation-detail-view.fxml", "Conversation");
    }

    private void loadConversations() {
        Task<List<ConversationResponse>> task = new Task<>() {
            @Override
            protected List<ConversationResponse> call() throws Exception {
                return chatService.getMyConversations();
            }
        };

        task.setOnSucceeded(event -> {
            conversationsListView.getItems().setAll(task.getValue());
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
}
