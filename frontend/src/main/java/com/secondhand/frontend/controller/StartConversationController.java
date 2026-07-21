package com.secondhand.frontend.controller;

import com.secondhand.frontend.service.ApiException;
import com.secondhand.frontend.service.ChatService;
import com.secondhand.frontend.service.dto.MessageResponse;
import com.secondhand.frontend.service.dto.StartConversationRequest;
import com.secondhand.frontend.util.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class StartConversationController {

    @FXML
    private TextField sellerUsernameField;

    @FXML
    private TextField advertisementIdField;

    @FXML
    private TextArea messageField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button sendButton;

    private final ChatService chatService = new ChatService();

    @FXML
    public void initialize() {
        Long adId = com.secondhand.frontend.util.NavigationContext.getTargetAdvertisementId();
        String seller = com.secondhand.frontend.util.NavigationContext.getTargetSellerUsername();

        if (adId != null) advertisementIdField.setText(adId.toString());
        if (seller != null) sellerUsernameField.setText(seller);
    }

    @FXML
    private void handleSend() {
        hideError();

        String sellerUsername = sellerUsernameField.getText() == null ? "" : sellerUsernameField.getText().trim();
        Long advertisementId = parseLongOrNull(advertisementIdField.getText());
        String content = messageField.getText() == null ? "" : messageField.getText().trim();

        if (sellerUsername.isEmpty()) {
            showError("Please enter the seller's username");
            return;
        }
        if (advertisementId == null) {
            showError("Please enter a valid advertisement ID");
            return;
        }
        if (content.isEmpty()) {
            showError("Message cannot be empty");
            return;
        }

        StartConversationRequest requestBody = new StartConversationRequest(advertisementId, sellerUsername, content);

        sendButton.setDisable(true);

        Task<MessageResponse> task = new Task<>() {
            @Override
            protected MessageResponse call() throws Exception {
                return chatService.startConversation(requestBody);
            }
        };

        task.setOnSucceeded(event -> {
            sendButton.setDisable(false);
            SceneManager.switchTo("/com/secondhand/frontend/view/conversations-list-view.fxml", "My Conversations");
        });

        task.setOnFailed(event -> {
            sendButton.setDisable(false);
            Throwable ex = task.getException();
            if (ex instanceof ApiException apiEx) {
                showError(apiEx.getMessage());
            } else {
                showError("Could not connect to the server Make sure the backend is running");
            }
        });

        new Thread(task).start();
    }

    private Long parseLongOrNull(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
    }
}
