package com.secondhand.frontend;

import javafx.fxml.FXML;
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

    /**
     * Populates the advertisement card with data retrieved from the server.
     *
     * @param title        The title of the advertisement.
     * @param price        The formatted price string.
     * @param locationTime The formatted string containing the city and time elapsed.
     * @param imageUrl     The URL or relative path to the advertisement's image.
     */
    public void setAdData(String title, String price, String locationTime, String imageUrl) {
        titleLabel.setText(title);
        priceLabel.setText(price);
        locationTimeLabel.setText(locationTime);
        try {
            adImage.setImage(new Image(imageUrl));
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imageUrl);
        }
    }

    /**
     * Handles the click event on the favorite button.
     * Toggles the favorite state and updates the UI accordingly.
     */
    @FXML
    public void handleFavoriteClick() {
        isFavorite = !isFavorite;
        if (isFavorite) {
            favoriteButton.setText("♥");
            favoriteButton.setStyle("-fx-text-fill: red;");
        } else {
            favoriteButton.setText("♡");
            favoriteButton.setStyle("-fx-text-fill: black;");
        }
    }
}
