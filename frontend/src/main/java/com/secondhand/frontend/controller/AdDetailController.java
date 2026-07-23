package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.service.RatingService;
import com.secondhand.frontend.service.dto.SellerRatingSummary;
import com.secondhand.frontend.session.SessionManager;
import com.secondhand.frontend.util.NavigationContext;
import com.secondhand.frontend.util.SceneManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class AdDetailController implements Initializable {

    private static Advertisement currentAd;

    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private Label categoryCityLabel;
    @FXML private Label ownerLabel;
    @FXML private Label sellerRatingLabel;
    @FXML private Text descriptionText;

    private final RatingService ratingService = new RatingService();

    public static void setSelectedAd(Advertisement ad) {
        currentAd = ad;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (currentAd != null) {
            DecimalFormat priceFormat = new DecimalFormat("#,###");
            titleLabel.setText(currentAd.getTitle());
            priceLabel.setText(priceFormat.format(currentAd.getPrice()) + " Tomans");

            String cityName = (currentAd.getCity() != null) ? currentAd.getCity().getName() : "Unknown";
            String catName = (currentAd.getCategory() != null) ? currentAd.getCategory().getName() : "Uncategorized";
            categoryCityLabel.setText("Category: " + catName + " | City: " + cityName);

            if (ownerLabel != null && currentAd.getOwnerUsername() != null) {
                ownerLabel.setText("Seller: " + currentAd.getOwnerUsername());
            }

            if (descriptionText != null && currentAd.getDescription() != null) {
                descriptionText.setText(currentAd.getDescription());
            }

            loadSellerRating();
        }
    }

    private void loadSellerRating() {
        if (currentAd.getOwnerId() == null) {
            if (sellerRatingLabel != null) {
                sellerRatingLabel.setText("⭐ Rating: --");
            }
            return;
        }

        Task<SellerRatingSummary> task = new Task<>() {
            @Override
            protected SellerRatingSummary call() throws Exception {
                return ratingService.getSellerRatingSummary(currentAd.getOwnerId());
            }
        };

        task.setOnSucceeded(event -> {
            SellerRatingSummary summary = task.getValue();
            if (summary != null && summary.getTotalRatings() > 0) {
                String ratingText = String.format("⭐ %.1f/5 (%d reviews)", summary.getAverageScore(), summary.getTotalRatings());
                Platform.runLater(() -> sellerRatingLabel.setText(ratingText));
            } else {
                Platform.runLater(() -> sellerRatingLabel.setText("⭐ No ratings"));
            }
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> sellerRatingLabel.setText("⭐ Rating: --"));
        });

        new Thread(task).start();
    }

    @FXML
    public void onBackAction() {
        SceneManager.switchTo("/com/secondhand/frontend/view/main-view.fxml", "Second Hand Market");
    }

    @FXML
    private void handleChatWithSeller() {
        if (!isUserLoggedIn()) {
            redirectToLogin();
            return;
        }

        if (currentAd != null) {
            System.out.println("[DEBUG] Chat button clicked for Ad ID: " + currentAd.getId()
                    + " | Owner: " + currentAd.getOwnerUsername());

            NavigationContext.setTargetAdvertisementId(currentAd.getId());
            NavigationContext.setTargetAdvertisementTitle(currentAd.getTitle());
            NavigationContext.setTargetSellerUsername(currentAd.getOwnerUsername());

            SceneManager.switchTo("/com/secondhand/frontend/view/conversations-list-view.fxml", "My Chats");
        }
    }

    @FXML
    private void handleRateSeller() {
        if (!isUserLoggedIn()) {
            redirectToLogin();
            return;
        }

        if (currentAd != null) {
            NavigationContext.setTargetAdvertisementId(currentAd.getId());
            NavigationContext.setTargetAdvertisementTitle(currentAd.getTitle());
            NavigationContext.setTargetSellerUsername(currentAd.getOwnerUsername());
            NavigationContext.setTargetSellerId(currentAd.getOwnerId());

            SceneManager.showAsPopup("/com/secondhand/frontend/view/rate-seller-view.fxml", "Rate Seller");
        }
    }

    private boolean isUserLoggedIn() {
        String token = SessionManager.getInstance().getToken();
        return token != null && !token.trim().isEmpty();
    }

    private void redirectToLogin() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText("Authentication Required");
        alert.setContentText("Please log in to your account first to access this feature.");
        alert.showAndWait();

        SceneManager.switchTo("/com/secondhand/frontend/view/login-view.fxml", "Login");
    }

}