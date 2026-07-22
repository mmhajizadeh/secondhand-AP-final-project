package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.service.ApiService;
import com.secondhand.frontend.util.NavigationContext;
import com.secondhand.frontend.util.SceneManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FavoritesController implements Initializable {
    @FXML
    private ListView<Advertisement> favoritesListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListView();
        loadFavorites();
    }

    private void setupListView() {
        favoritesListView.setCellFactory(param -> new ListCell<>() {
           @Override
           protected void updateItem(Advertisement ad, boolean empty) {
               super.updateItem(ad, empty);
               if (empty || ad == null) {
                   setText(null);
                   setGraphic(null);
               } else {
                   HBox root = new HBox(15);
                   root.setStyle("-fx-padding: 15; -fx-background-color: transparent; -fx-border-color: #BDC3C7; -fx-border-width: 0 0 1 0;");
                   root.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                   Label titleLabel = new Label(ad.getTitle() + " - " + ad.getPrice() + " تومان");
                   titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                   Region spacer = new Region();
                   HBox.setHgrow(spacer, Priority.ALWAYS);

                   Button viewBtn = new Button("View Ad");
                   viewBtn.getStyleClass().add("button");
                   viewBtn.setOnAction(e -> handleViewAd(ad));

                   Button removeBtn = new Button("Remove");
                   removeBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-cursor: hand;");
                   removeBtn.setOnAction(e -> handleRemove(ad));

                   root.getChildren().addAll(titleLabel, spacer, viewBtn, removeBtn);
                   setGraphic(root);
               }
           }
        });
    }

    private void loadFavorites() {
        try {
            List<Advertisement> favorites = ApiService.getFavorites();
            favoritesListView.setItems(FXCollections.observableList(favorites));
        } catch (Exception e) {
            showError("Failed to load favorites: " + e.getMessage());
        }
    }

    private void  handleViewAd(Advertisement ad) {
        AdDetailController.setSelectedAd(ad);
        NavigationContext.setTargetAdvertisementId(ad.getId());
        NavigationContext.setTargetSellerUsername(ad.getOwnerUsername());
        SceneManager.showAsPopup("/com/secondhand/frontend/view/ad-detail-view.fxml", "Advertisement Details");
    }

    private void handleRemove(Advertisement ad) {
        try {
            ApiService.removeFavorite(ad.getId());
            loadFavorites(); // Refresh list
        } catch (Exception e) {
            showError("Failed to remove favorite: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        loadFavorites();
    }

    @FXML
    private void handleBack() {
        SceneManager.switchTo("/com/secondhand/frontend/view/main-view.fxml", "Second Hand Market");
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
