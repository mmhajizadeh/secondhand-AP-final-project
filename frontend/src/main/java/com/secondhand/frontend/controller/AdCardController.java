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
     * @param imageUrl     The URL or relative path to the advertisement's image.
     */
    public void setAdData(Long adId, String title, String price, String locationTime, String imageUrl) {
        this.currentAdId = adId;
        titleLabel.setText(title);
        priceLabel.setText(price);
        locationTimeLabel.setText(locationTime);

        titleLabel.setStyle("-fx-text-fill: #2C3E50; -fx-font-weight: bold; -fx-font-size: 14px;");
        priceLabel.setStyle("-fx-text-fill: #27AE60; -fx-font-weight: bold; -fx-font-size: 14px;");
        locationTimeLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 12px;");

        try {
            adImage.setImage(new Image(imageUrl));
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imageUrl);
        }
    }

    /**
     * Handles the click event on the favorite button.
     * Toggles the favorite state and updates the UI accordingly.
     * Updates the UI heart icon color and communicates with the backend API.
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
                favoriteButton.setText("♥");
                favoriteButton.setStyle("-fx-text-fill: red;");
            } else {
                ApiService.removeFavorite(currentAdId);
                isFavorite = false;
                favoriteButton.setText("♡");
                favoriteButton.setStyle("-fx-text-fill: black;");
            }
        } catch (Exception e) {
            showAlert("Failed to update favorites: " + e.getMessage());
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
