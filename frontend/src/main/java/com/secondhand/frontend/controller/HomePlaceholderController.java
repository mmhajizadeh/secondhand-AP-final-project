package com.secondhand.frontend.controller;

import com.secondhand.frontend.session.SessionManager;
import com.secondhand.frontend.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Temporary placeholder screen shown right after a successful login / register
 * Will be replaced once the main listings screen (teammates task) is ready.
 */
public class HomePlaceholderController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button adminDashboardButton;

    @FXML
    public void initialize() {
        var user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
        }

        boolean isAdmin = SessionManager.getInstance().isAdmin();
        adminDashboardButton.setVisible(isAdmin);
        adminDashboardButton.setManaged(isAdmin);
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().clearSession();
        SceneManager.switchTo("/com/secondhand/frontend/view/login-view.fxml", "Login");
    }

    @FXML
    private void handleGoToRateSeller() {
        SceneManager.switchTo("/com/secondhand/frontend/view/rate-seller-view.fxml", "Rate Seller");
    }

    @FXML
    private void handleGoToConversations() {
        SceneManager.switchTo("/com/secondhand/frontend/view/conversations-list-view.fxml", "My Conversations");
    }

    @FXML
    private void handleGoToAdminDashboard() {
        SceneManager.switchTo("/com/secondhand/frontend/view/admin-home-view.fxml", "Admin Dashboard");
    }
}
