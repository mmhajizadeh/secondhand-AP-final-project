package com.secondhand.frontend.controller;
import com.secondhand.frontend.service.ApiException;
import com.secondhand.frontend.service.RatingService;
import com.secondhand.frontend.service.dto.RatingRequest;
import com.secondhand.frontend.service.dto.RatingResponse;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
public class RateSellerController {

    @FXML
    private TextField sellerIdField;

    @FXML
    private TextField advertisementIdField;

    @FXML
    private ChoiceBox<Integer> scoreChoiceBox;

    @FXML
    private TextArea commentField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel;

    @FXML
    private Button submitButton;

    private final RatingService ratingService = new RatingService();

    @FXML
    public void initialize() {
        scoreChoiceBox.getItems().addAll(1, 2, 3, 4, 5);
        scoreChoiceBox.setValue(5);
    }

    @FXML
    private void handleSubmit() {
        hideMessages();
        Long sellerId = parseLongOrNull(sellerIdField.getText());
        Long advertisementId = parseLongOrNull(advertisementIdField.getText());
        Integer score = scoreChoiceBox.getValue();

        if (sellerId == null) {
            showError("Please enter a valid seller ID");
            return;
        }
        if (advertisementId == null) {
            showError("Please enter a valid advertisement ID");
            return;
        }
        if (score == null) {
            showError("Please select a score");
            return;
        }

        String comment = commentField.getText() == null ? null : commentField.getText().trim();
        if (comment != null && comment.isEmpty()) {
            comment = null;
        }
        RatingRequest requestBody = new RatingRequest(sellerId, advertisementId, score, comment);

        submitButton.setDisable(true);

        Task<RatingResponse> task = new Task<>() {
            @Override
            protected RatingResponse call() throws Exception {
                return ratingService.submitRating(requestBody);
            }
        };

        task.setOnSucceeded(event -> {
            submitButton.setDisable(false);
            showSuccess("Rating submitted successfully!!");
            clearForm();
        });

        task.setOnFailed(event -> {
            submitButton.setDisable(false);
            Throwable ex = task.getException();
            if (ex instanceof ApiException apiEx) {
                showError(apiEx.getMessage());
            } else {
                showError("Could not connect to the server. Make sure the backend is running.");
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

    private void clearForm() {
        sellerIdField.clear();
        advertisementIdField.clear();
        commentField.clear();
        scoreChoiceBox.setValue(5);
    }

    private void showError(String message) {
        successLabel.setVisible(false);
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        errorLabel.setVisible(false);
        successLabel.setText(message);
        successLabel.setVisible(true);
    }

    private void hideMessages() {
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }
}
