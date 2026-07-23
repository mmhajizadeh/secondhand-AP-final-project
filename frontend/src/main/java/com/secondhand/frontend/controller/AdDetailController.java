package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.service.RatingService;
import com.secondhand.frontend.service.dto.RatingResponse;
import com.secondhand.frontend.service.dto.SellerRatingSummary;
import com.secondhand.frontend.session.SessionManager;
import com.secondhand.frontend.util.NavigationContext;
import com.secondhand.frontend.util.SceneManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for managing the detailed view of an advertisement,
 * seller ratings, and buyer comments.
 */
public class AdDetailController implements Initializable {

    private static Advertisement currentAd;

    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private Label categoryCityLabel;
    @FXML private Label ownerLabel;
    @FXML private Label sellerRatingLabel;
    @FXML private Text descriptionText;
    @FXML private VBox commentsVBox;
    @FXML private Button chatButton;
    @FXML private Button editButton;
    @FXML private HBox imageGalleryHBox;

    private final RatingService ratingService = new RatingService();

    /**
     * Sets the target advertisement to be displayed in the detail view.
     *
     * @param ad The advertisement instance selected by the user.
     */
    public static void setSelectedAd(Advertisement ad) {
        currentAd = ad;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (currentAd != null) {
            DecimalFormat priceFormat = new DecimalFormat("#,###");
            titleLabel.setText(currentAd.getTitle());

            if (currentAd.getPrice() != null) {
                priceLabel.setText(priceFormat.format(currentAd.getPrice()) + " Tomans");
            } else {
                priceLabel.setText("-- Tomans");
            }

            String cityName = (currentAd.getCity() != null) ? currentAd.getCity().getName() : "Unknown";
            String catName = (currentAd.getCategory() != null) ? currentAd.getCategory().getName() : "Uncategorized";
            categoryCityLabel.setText("Category: " + catName + " | City: " + cityName);

            if (ownerLabel != null && currentAd.getOwnerUsername() != null) {
                ownerLabel.setText("Seller: " + currentAd.getOwnerUsername());
            }

            if (descriptionText != null && currentAd.getDescription() != null) {
                descriptionText.setText(currentAd.getDescription());
            }

            boolean isActive = "ACTIVE".equalsIgnoreCase(currentAd.getStatus());

            String currentLoggedInUser = getLoggedInUsername();
            boolean isOwner = currentLoggedInUser != null && currentLoggedInUser.equals(currentAd.getOwnerUsername());

            boolean isSold = "SOLD".equalsIgnoreCase(currentAd.getStatus());

            if (chatButton != null) {
                chatButton.setVisible(isActive && !isOwner);
                chatButton.setManaged(isActive && !isOwner);
            }

            if (editButton != null) {
                boolean canEdit = isOwner && !isSold;
                editButton.setVisible(canEdit);
                editButton.setManaged(canEdit);
            }

            if (imageGalleryHBox != null) {
                imageGalleryHBox.getChildren().clear();

                if (currentAd.getImages() != null && !currentAd.getImages().isEmpty()) {
                    for (String base64Image : currentAd.getImages()) {
                        try {
                            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
                            Image img = new Image(new java.io.ByteArrayInputStream(imageBytes));

                            ImageView imageView = new ImageView(img);
                            imageView.setFitHeight(200);
                            imageView.setFitWidth(250);
                            imageView.setPreserveRatio(true);
                            imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");

                            imageGalleryHBox.getChildren().add(imageView);
                        } catch (Exception e) {
                            System.err.println("Error decoding image in AdDetailController.");
                        }
                    }
                }
            }

            loadSellerRating();
        }
    }

    /**
     * Asynchronously loads the seller's rating summary and buyer comments.
     */
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
            if (summary != null) {
                if (summary.getTotalRatings() > 0) {
                    String ratingText = String.format(Locale.US, "⭐ %.1f/5 (%d reviews)", summary.getAverageScore(), summary.getTotalRatings());
                    Platform.runLater(() -> sellerRatingLabel.setText(ratingText));
                } else {
                    Platform.runLater(() -> sellerRatingLabel.setText("⭐ No ratings"));
                }

                // Render Buyer Comments
                Platform.runLater(() -> {
                    if (commentsVBox != null) {
                        commentsVBox.getChildren().clear();

                        if (summary.getRatings() != null && !summary.getRatings().isEmpty()) {
                            for (RatingResponse r : summary.getRatings()) {
                                VBox commentCard = new VBox(8);
                                commentCard.setStyle("-fx-background-color: white; -fx-padding: 12; -fx-border-color: #DCDDE1; -fx-border-radius: 6;");

                                String rater = (r.getRaterUsername() != null && !r.getRaterUsername().isBlank())
                                        ? r.getRaterUsername() : "Buyer";

                                Label userLabel = new Label("👤 " + rater + " | ⭐ " + r.getScore() + "/5");
                                userLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C3E50;");

                                Label textLabel = new Label((r.getComment() != null && !r.getComment().isBlank())
                                        ? r.getComment() : "No text comment.");
                                textLabel.setWrapText(true);
                                textLabel.setStyle("-fx-text-fill: #7F8C8D;");

                                Hyperlink adLink = new Hyperlink("View Advertisement");
                                adLink.setStyle("-fx-font-size: 11px; -fx-text-fill: #3498DB; -fx-padding: 0;");

                                adLink.setOnAction(e -> {
                                    Task<Advertisement> fetchAdTask = new Task<>() {
                                        @Override
                                        protected Advertisement call() throws Exception {
                                            return com.secondhand.frontend.service.ApiService.getAdById(r.getAdvertisementId());
                                        }
                                    };

                                    fetchAdTask.setOnSucceeded(fetchEvent -> {
                                        Advertisement realAd = fetchAdTask.getValue();
                                        if (realAd != null) {
                                            Platform.runLater(() -> {
                                                setSelectedAd(realAd);
                                                NavigationContext.setTargetAdvertisementId(realAd.getId());
                                                if (realAd.getOwnerUsername() != null) {
                                                    NavigationContext.setTargetSellerUsername(realAd.getOwnerUsername());
                                                }
                                                SceneManager.showAsPopup("/com/secondhand/frontend/view/ad-detail-view.fxml", "Advertisement Details");
                                            });
                                        }
                                    });

                                    fetchAdTask.setOnFailed(fetchEvent -> {
                                        System.err.println("Could not fetch ad details for ID: " + r.getAdvertisementId());
                                        if (fetchAdTask.getException() != null) {
                                            fetchAdTask.getException().printStackTrace();
                                        }
                                    });

                                    new Thread(fetchAdTask).start();
                                });

                                commentCard.getChildren().addAll(userLabel, adLink, textLabel);
                                commentsVBox.getChildren().add(commentCard);
                            }
                        } else {
                            Label emptyLabel = new Label("No comments yet.");
                            emptyLabel.setStyle("-fx-text-fill: #7F8C8D;");
                            commentsVBox.getChildren().add(emptyLabel);
                        }
                    }
                });
            }
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> sellerRatingLabel.setText("⭐ Rating: --"));
        });

        new Thread(task).start();
    }

    /**
     * Handles the back navigation action triggered by the user.
     */
    @FXML
    public void onBackAction(ActionEvent event) {
        NavigationContext.setTargetAdvertisementId(null);
        NavigationContext.setTargetSellerUsername(null);

        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();

        if (currentStage.equals(SceneManager.getPrimaryStage())) {
            SceneManager.switchTo("/com/secondhand/frontend/view/main-view.fxml", "Second Hand Market");
        } else {
            currentStage.close();
        }
    }

    /**
     * Navigates the user to the chat screen with the seller.
     */
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
            NavigationContext.setTargetSellerId(currentAd.getOwnerId());

            SceneManager.switchTo("/com/secondhand/frontend/view/conversations-list-view.fxml", "My Chats");
        }
    }

    /**
     * Navigates the owner to the ad creation form in edit mode
     * and closes the current popup window.
     */
    @FXML
    private void handleEditAd() {
        if (currentAd != null) {
            NewAdController.setAdToEdit(currentAd);

            if (editButton != null && editButton.getScene() != null) {
                Stage currentStage = (Stage) editButton.getScene().getWindow();
                if (!currentStage.equals(SceneManager.getPrimaryStage())) {
                    currentStage.close();
                }
            }

            SceneManager.switchTo("/com/secondhand/frontend/view/new-ad-view.fxml", "Edit Advertisement");
        }
    }

    /**
     * Checks if a user session is active.
     *
     * @return True if logged in, false otherwise.
     */
    private boolean isUserLoggedIn() {
        String token = SessionManager.getInstance().getToken();
        return token != null && !token.trim().isEmpty();
    }

    /**
     * Extracts the username from the current JWT token payload.
     * @return the logged-in username, or null if not logged in.
     */
    private String getLoggedInUsername() {
        String token = SessionManager.getInstance().getToken();
        if (token != null && !token.trim().isEmpty()) {
            try {
                String[] chunks = token.split("\\.");
                if (chunks.length >= 2) {
                    String payload = new String(java.util.Base64.getUrlDecoder().decode(chunks[1]));
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    java.util.Map<String, Object> map = mapper.readValue(payload, java.util.Map.class);
                    return (String) map.get("sub"); // Spring Security stores username in 'sub' by default
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Redirects unauthenticated users to the login screen.
     */
    private void redirectToLogin() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText("Authentication Required");
        alert.setContentText("Please log in to your account first to access this feature.");
        alert.showAndWait();

        SceneManager.switchTo("/com/secondhand/frontend/view/login-view.fxml", "Login");
    }
}