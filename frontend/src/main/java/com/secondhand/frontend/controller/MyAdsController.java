package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.service.ApiService;
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

public class MyAdsController implements Initializable {

    @FXML
    private ListView<Advertisement> myAdsListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListView();
        loadMyAds();
    }

    private void setupListView() {
        myAdsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Advertisement ad, boolean empty) {
                super.updateItem(ad, empty);
                if (empty || ad == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox root = new HBox(15);
                    root.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-border-color: #BDC3C7; -fx-border-width: 0 0 1 0;");
                    root.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                    Label titleLabel = new Label(ad.getTitle() + " (" + ad.getStatus() + ")");
                    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2C3E50;");

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button soldBtn = new Button("✔ Mark Sold");
                    soldBtn.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-cursor: hand;");
                    soldBtn.setOnAction(e -> handleMarkSold(ad));
                    soldBtn.setDisable("SOLD".equals(ad.getStatus()));

                    Button deleteBtn = new Button("✖ Delete");
                    deleteBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-cursor: hand;");
                    deleteBtn.setOnAction(e -> handleDelete(ad));

                    root.getChildren().addAll(titleLabel, spacer, soldBtn, deleteBtn);
                    setGraphic(root);
                }
            }
        });
    }

    private void loadMyAds() {
        try {
            List<Advertisement> myAds = ApiService.getMyAds();
            myAdsListView.setItems(FXCollections.observableArrayList(myAds));
        } catch (Exception e) {
            showError("Failed to load your ads: " + e.getMessage());
        }
    }

    private void handleMarkSold(Advertisement ad) {
        try {
            ApiService.markAdAsSold(ad.getId());
            showSuccess("Ad marked as sold!");
            loadMyAds();
        } catch (Exception e) {
            showError("Failed to update ad status: " + e.getMessage());
        }
    }

    private void handleDelete(Advertisement ad) {
        try {
            ApiService.deleteAd(ad.getId());
            showSuccess("Ad deleted successfully!");
            loadMyAds();
        } catch (Exception e) {
            showError("Failed to delete ad: " + e.getMessage());
        }
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

    private void showSuccess(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}