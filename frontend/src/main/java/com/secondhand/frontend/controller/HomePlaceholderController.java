package com.secondhand.frontend.controller;

import com.secondhand.frontend.session.SessionManager;
import com.secondhand.frontend.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Temporary placeholder screen shown right after a successful login / register
 * Will be replaced once the main listings screen (teammates task) is ready.
 */
public class HomePlaceholderController {

    @FXML
    private Label welcomeLabel;
    @FXML
    public void initialize() {
        var user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
        }
    }
    @FXML
    private void handleLogout() {
        SessionManager.getInstance().clearSession();
        SceneManager.switchTo("/com/secondhand/frontend/view/login-view.fxml", "Login");
    }
}
