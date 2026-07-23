package com.secondhand.frontend.controller;

import com.secondhand.frontend.service.ApiException;
import com.secondhand.frontend.service.RatingService;
import com.secondhand.frontend.service.dto.RatingRequest;
import com.secondhand.frontend.service.dto.RatingResponse;
import com.secondhand.frontend.util.NavigationContext;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controller class for managing seller ratings and feedback submissions.
 */
public class RateSellerController {

    @FXML
    private Label infoLabel;

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

    private Long sellerId;
    private Long advertisementId;

    /**
     * Initializes the rating view with default values and retrieves seller/advertisement details.
     */
    @FXML
    public void initialize() {
        scoreChoiceBox.getItems().addAll(1, 2, 3, 4, 5);
        scoreChoiceBox.setValue(5);

        this.advertisementId = NavigationContext.getTargetAdvertisementId();
        this.sellerId = NavigationContext.getTargetSellerId();
        String sellerUsername = NavigationContext.getTargetSellerUsername();
        String adTitle = NavigationContext.getTargetAdvertisementTitle();

        if (sellerUsername != null && !sellerUsername.isBlank()) {
            if (adTitle != null && !adTitle.isBlank()) {
                infoLabel.setText("Rating for Seller: " + sellerUsername + " (" + adTitle + ")");
            } else {
                infoLabel.setText("Rating for Seller: " + sellerUsername);
            }
        } else if (adTitle != null && !adTitle.isBlank()) {
            infoLabel.setText("Rating for: " + adTitle);
        } else {
            infoLabel.setText("Rate Seller");
        }
    }

    /**
     * Handles the rating submission process asynchronously.
     */
    @FXML
    private void handleSubmit() {
        hideMessages();

        Integer score = scoreChoiceBox.getValue();

        // Validate required parameters before making an API request
        if (sellerId == null || advertisementId == null) {
            showError("No advertisement or seller selected. Please rate from advertisement page.");
            return;
        }
        if (score == null) {
            showError("Please select a valid score (1 to 5)");
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
            showSuccess("Rating submitted successfully!");
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

    /**
     * Clears input fields after successful submission.
     */
    private void clearForm() {
        commentField.clear();
        scoreChoiceBox.setValue(5);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message text.
     */
    private void showError(String message) {
        successLabel.setVisible(false);
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Displays a success message to the user.
     *
     * @param message The success message text.
     */
    private void showSuccess(String message) {
        errorLabel.setVisible(false);
        successLabel.setText(message);
        successLabel.setVisible(true);
    }

    /**
     * Hides both error and success message labels.
     */
    private void hideMessages() {
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }
}