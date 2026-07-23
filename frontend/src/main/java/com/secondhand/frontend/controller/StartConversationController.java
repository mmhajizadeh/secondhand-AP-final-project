package com.secondhand.frontend.controller;

import com.secondhand.frontend.service.ApiException;
import com.secondhand.frontend.service.ChatService;
import com.secondhand.frontend.service.dto.MessageResponse;
import com.secondhand.frontend.service.dto.StartConversationRequest;
import com.secondhand.frontend.util.NavigationContext;
import com.secondhand.frontend.util.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class StartConversationController {

    @FXML private Label targetInfoLabel;
    @FXML private TextArea messageField;
    @FXML private Label errorLabel;
    @FXML private Button sendButton;

    private final ChatService chatService = new ChatService();
    private Long advertisementId;
    private String sellerUsername;

    @FXML
    public void initialize() {
        // دریافت اطلاعات آگهی
        this.advertisementId = NavigationContext.getTargetAdvertisementId();
        this.sellerUsername = NavigationContext.getTargetSellerUsername();

        // نمایش اطلاعات بالای صفحه
        if (sellerUsername != null && advertisementId != null) {
            targetInfoLabel.setText("To seller: " + sellerUsername + " (Ad #" + advertisementId + ")");
        } else if (sellerUsername != null) {
            targetInfoLabel.setText("To seller: " + sellerUsername);
        } else if (advertisementId != null) {
            targetInfoLabel.setText("Regarding Ad #" + advertisementId);
        } else {
            targetInfoLabel.setText("Start a new conversation");
        }

        // قابلیت ارسال پیام با کلید Enter
        if (messageField != null) {
            messageField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    if (event.isShiftDown()) {
                        // Shift + Enter -> خط بعدی
                    } else {
                        event.consume();
                        handleSend();
                    }
                }
            });
        }
    }

    @FXML
    private void handleSend() {
        hideError();
        String content = messageField.getText() == null ? "" : messageField.getText().trim();

        if (sellerUsername == null || sellerUsername.isBlank()) {
            showError("Seller information is missing. Please open chat from the advertisement page.");
            return;
        }
        if (advertisementId == null) {
            showError("Advertisement ID is missing. Please open chat from the advertisement page.");
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
            // پاک کردن Context بعد از ارسال موفق پیام
            NavigationContext.setTargetAdvertisementId(null);
            NavigationContext.setTargetSellerUsername(null);

            SceneManager.switchTo("/com/secondhand/frontend/view/conversations-list-view.fxml", "My Conversations");
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

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
    }
}