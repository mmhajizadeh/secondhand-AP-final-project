package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.CurrentUser;
import com.secondhand.frontend.service.ApiException;
import com.secondhand.frontend.service.AuthService;
import com.secondhand.frontend.service.dto.AuthResponse;
import com.secondhand.frontend.service.dto.LoginRequest;
import com.secondhand.frontend.session.SessionManager;
import com.secondhand.frontend.util.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty");
            return;
        }

        hideError();
        loginButton.setDisable(true);
        // Runs on a background thread so the UI does not freeze while waiting for the backend.
        Task<AuthResponse> task = new Task<>() {
            @Override
            protected AuthResponse call() throws Exception {
                return authService.login(new LoginRequest(username, password));
            }
        };

        task.setOnSucceeded(event -> {
            loginButton.setDisable(false);
            AuthResponse response = task.getValue();
            SessionManager.getInstance().startSession(
                    response.getToken(),
                    new CurrentUser(response.getUserId(), response.getUsername(), response.getFullName(), response.getRole())
            );
            SceneManager.switchTo("/com/secondhand/frontend/view/home-placeholder-view.fxml", "Second-Hand Marketplace");
        });
        task.setOnFailed(event -> {
            loginButton.setDisable(false);
            Throwable ex = task.getException();
            if (ex instanceof ApiException apiEx) {
                showError(apiEx.getMessage());
            } else {
                showError("Could not connect to the server. Make sure the backend is running.");
            }
        });

        new Thread(task).start();
    }

    @FXML
    private void goToRegister() {
        SceneManager.switchTo("/com/secondhand/frontend/view/register-view.fxml", "Register");
    }
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
    }
}
