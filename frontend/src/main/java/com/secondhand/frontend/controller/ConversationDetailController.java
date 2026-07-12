package com.secondhand.frontend.controller;

import com.secondhand.frontend.service.ApiException;
import com.secondhand.frontend.service.ChatService;
import com.secondhand.frontend.service.dto.MessageResponse;
import com.secondhand.frontend.service.dto.SendMessageRequest;
import com.secondhand.frontend.session.SessionManager;
import com.secondhand.frontend.util.NavigationContext;
import com.secondhand.frontend.util.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class ConversationDetailController {

    @FXML
    private Label titleLabel;

    @FXML
    private ListView<MessageResponse> messagesListView;

    @FXML
    private TextField newMessageField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button sendButton;

    private final ChatService chatService = new ChatService();
    private Long conversationId;

    @FXML
    public void initialize() {
        conversationId = NavigationContext.getCurrentConversationId();
        String title = NavigationContext.getCurrentConversationTitle();
        titleLabel.setText(title != null ? title : "Conversation");

        messagesListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(MessageResponse item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String me = SessionManager.getInstance().getCurrentUser().getUsername();
                    String label = me.equals(item.getSenderUsername()) ? "You" : item.getSenderUsername();
                    setText(label + ": " + item.getContent());
                }
            }
        });

        if (conversationId != null) {
            loadMessages();
        }
    }

    @FXML
    private void handleSend() {
        hideError();

        String content = newMessageField.getText() == null ? "" : newMessageField.getText().trim();
        if (content.isEmpty()) {
            showError("Message cannot be empty");
            return;
        }
        if (conversationId == null) {
            showError("No conversation selected");
            return;
        }

        sendButton.setDisable(true);

        Task<MessageResponse> task = new Task<>() {
            @Override
            protected MessageResponse call() throws Exception {
                return chatService.sendMessage(conversationId, new SendMessageRequest(content));
            }
        };

        task.setOnSucceeded(event -> {
            sendButton.setDisable(false);
            newMessageField.clear();
            loadMessages();
        });

        task.setOnFailed(event -> {
            sendButton.setDisable(false);
            Throwable ex = task.getException();
            if (ex instanceof ApiException apiEx) {
                showError(apiEx.getMessage());
            } else {
                showError("Could not connect to the server. Make sure the backend is running.");
            }
        });

        new Thread(task).start();
    }

    @FXML
    private void handleBack() {
        SceneManager.switchTo("/com/secondhand/frontend/view/conversations-list-view.fxml", "My Conversations");
    }

    private void loadMessages() {
        Task<List<MessageResponse>> task = new Task<>() {
            @Override
            protected List<MessageResponse> call() throws Exception {
                return chatService.getMessages(conversationId);
            }
        };

        task.setOnSucceeded(event -> messagesListView.getItems().setAll(task.getValue()));

        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            if (ex instanceof ApiException apiEx) {
                showError(apiEx.getMessage());
            } else {
                showError("Could not connect to the server");
            }
        });

        new Thread(task).start();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
    }
}
