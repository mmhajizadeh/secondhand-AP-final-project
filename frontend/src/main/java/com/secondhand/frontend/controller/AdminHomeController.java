package com.secondhand.frontend.controller;

import com.secondhand.frontend.util.SceneManager;
import javafx.fxml.FXML;

/**
 * Landing screen for the admin panel. Routes to the two admin sections:
 * user management ( this features scope ) and advertisement moderation
 * (teammates scope, see admin-ads-view.fxml).
 */
public class AdminHomeController {

    @FXML
    private void handleGoToUsers() {
        SceneManager.switchTo("/com/secondhand/frontend/view/admin-users-view.fxml", "Admin — Users");
    }

    @FXML
    private void handleGoToAds() {
        // Teammate's screen: pending advertisement review/approval.
        SceneManager.switchTo("/com/secondhand/frontend/view/admin-ads-view.fxml", "Admin — Advertisements");
    }

    @FXML
    private void handleBack() {
        SceneManager.switchTo("/com/secondhand/frontend/view/main-view.fxml", "Second-Hand Marketplace");
    }
}
