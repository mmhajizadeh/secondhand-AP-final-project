package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.CurrentUser;
import com.secondhand.frontend.service.ApiException;
import com.secondhand.frontend.service.AuthService;
import com.secondhand.frontend.service.dto.AuthResponse;
import com.secondhand.frontend.service.dto.RegisterRequest;
import com.secondhand.frontend.session.SessionManager;
import com.secondhand.frontend.util.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button registerButton;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleRegister() {
        String username = trimOrEmpty(usernameField.getText());
        String fullName = trimOrEmpty(fullNameField.getText());
        String phone = trimOrEmpty(phoneField.getText());
        String email = trimOrEmpty(emailField.getText());
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (username.isEmpty() || fullName.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showError("Please fill in all required fields");
            return;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        hideError();
        registerButton.setDisable(true);

        RegisterRequest requestBody = new RegisterRequest(username, password, fullName, phone,
                email.isEmpty() ? null : email);

        Task<AuthResponse> task = new Task<>() {
            @Override
            protected AuthResponse call() throws Exception {
                return authService.register(requestBody);
            }
        };

        task.setOnSucceeded(event -> {
            registerButton.setDisable(false);
            AuthResponse response = task.getValue();
            SessionManager.getInstance().startSession(
                    response.getToken(),
                    new CurrentUser(response.getUserId(), response.getUsername(), response.getFullName(), response.getRole())
            );
            SceneManager.switchTo("/com/secondhand/frontend/view/home-placeholder-view.fxml", "Second-Hand Marketplace");
        });
        task.setOnFailed(event -> {
            registerButton.setDisable(false);
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
    private void goToLogin() {
        SceneManager.switchTo("/com/secondhand/frontend/view/login-view.fxml", "Login");
    }

    private String trimOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
    }
}
