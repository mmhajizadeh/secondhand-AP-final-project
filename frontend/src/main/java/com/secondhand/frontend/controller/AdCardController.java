package com.secondhand.frontend.controller;

import com.secondhand.frontend.service.ApiService;
import com.secondhand.frontend.session.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * Controller class for managing the individual advertisement card UI.
 * <p>
 * This class handles the data binding and user interactions for a single
 * advertisement card displayed within the main application grid.
 * </p>
 *
 * @since 1.0
 */
public class AdCardController {
    @FXML
    private ImageView adImage;

    @FXML
    private Label titleLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label locationTimeLabel;

    @FXML
    private Button favoriteButton;

    private boolean isFavorite = false;
    private Long currentAdId;

    /**
     * Populates the advertisement card with data retrieved from the server.
     *
     * @param adId         The unique identifier of the advertisement.
     * @param title        The title of the advertisement.
     * @param price        The formatted price string.
     * @param locationTime The formatted string containing the city and time elapsed.
     * @param base64Image  The advertisement's image.
     * @param isFavorite   Whether the current user has favorited this advertisement.
     */
    public void setAdData(Long adId, String title, String price, String locationTime, String base64Image, boolean isFavorite) {
        this.currentAdId = adId;
        this.isFavorite = isFavorite;

        titleLabel.setText(title);
        priceLabel.setText(price);
        locationTimeLabel.setText(locationTime);

        titleLabel.setStyle("-fx-text-fill: #2C3E50; -fx-font-weight: bold; -fx-font-size: 14px;");
        priceLabel.setStyle("-fx-text-fill: #27AE60; -fx-font-weight: bold; -fx-font-size: 14px;");
        locationTimeLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 12px;");

        updateFavoriteButtonUI();

        if (base64Image != null && base64Image.length() > 200) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                adImage.setImage(new Image(new ByteArrayInputStream(imageBytes)));
            } catch (Exception e) {
                System.err.println("Failed to decode base64 image for ad: " + adId);
            }
        } else {
            adImage.setImage(null);
        }
    }

    /**
     * Handles the click event on the favorite button.
     * Toggles the favorite state, updates backend, and refreshes the UI icon.
     */
    @FXML
    public void handleFavoriteClick() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            showAlert("Please login to add favorites.");
            return;
        }

        try {
            if (!isFavorite) {
                ApiService.addFavorite(currentAdId);
                isFavorite = true;
            } else {
                ApiService.removeFavorite(currentAdId);
                isFavorite = false;
            }
            updateFavoriteButtonUI();
        } catch (Exception e) {
            showAlert("Failed to update favorites: " + e.getMessage());
        }
    }

    /**
     * Updates the visual representation of the favorite button based on state.
     */
    private void updateFavoriteButtonUI() {
        if (isFavorite) {
            favoriteButton.setText("♥");
            favoriteButton.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            favoriteButton.setText("♡");
            favoriteButton.setStyle("-fx-text-fill: black;");
        }
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Favorites");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
