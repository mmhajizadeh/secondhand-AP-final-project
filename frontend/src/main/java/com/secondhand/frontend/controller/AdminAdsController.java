package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.service.ApiService;
import com.secondhand.frontend.util.SceneManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminAdsController implements Initializable{

    @FXML
    private ListView<Advertisement> pendingAdsListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListView();
        loadPendingAds();
    }

    private void setupListView(){
        pendingAdsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Advertisement ad, boolean empty) {
                super.updateItem(ad, empty);
                if(empty || ad == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox root = new HBox(15);
                    root.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-border-color: #BDC3C7; -fx-border-width: 0 0 1 0;");
                    root.setAlignment(Pos.CENTER_LEFT);

                    Label titleLabel = new Label("Title: " + ad.getTitle() + "  |  Seller: " + ad.getOwnerUsername());
                    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2C3E50;");

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button viewBtn = new Button("👁 View");
                    viewBtn.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
                    viewBtn.setOnAction(e -> handleView(ad));

                    Button approveBtn = new Button("✔ Approve");
                    approveBtn.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-cursor: hand;");
                    approveBtn.setOnAction(e -> handleApprove(ad));

                    Button rejectBtn = new Button("✖ Reject");
                    rejectBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-cursor: hand;");
                    rejectBtn.setOnAction(e -> handleReject(ad));

                    root.getChildren().addAll(titleLabel, spacer, viewBtn, approveBtn, rejectBtn);
                    setGraphic(root);
                }
            }
        });
    }

    private void loadPendingAds() {
        try {
            List<Advertisement> pending = ApiService.getPendingAds();
            pendingAdsListView.setItems(FXCollections.observableList(pending));
        } catch (Exception e) {
            showError("Failed to load pending ads: " + e.getMessage());
        }
    }

    private void handleView(Advertisement ad) {
        AdDetailController.setSelectedAd(ad);
        SceneManager.showAsPopup("/com/secondhand/frontend/view/ad-detail-view.fxml", "Review Ad Details");
    }

    private void handleApprove(Advertisement ad) {
        try {
            ApiService.approveAd(ad.getId());
            showSuccess("Ad approved successfully!");
            loadPendingAds();
        } catch (Exception e) {
            showError("Fialed to approve ad: " + e.getMessage());
        }
    }

    private void handleReject(Advertisement ad) {
        try {
            ApiService.rejectAd(ad.getId());
            showSuccess("Ad rejected successfully!");
            loadPendingAds();
        } catch (Exception e) {
            showError("Failed to reject ad: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchTo("/com/secondhand/frontend/view/admin-home-view.fxml", "Admin Dashboard");
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Action Failed");
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
