package com.secondhand.frontend.controller;

import com.secondhand.frontend.service.AdminService;
import com.secondhand.frontend.service.ApiException;
import com.secondhand.frontend.service.dto.UserSummaryResponse;
import com.secondhand.frontend.util.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;

/**
 * Single column list of users with a quick Block / Unblock action next to
 * each row, matching the UI pattern agreed with the teammates
 * advertisement-review screen (admin-ads-view.fxml) for a consistent look
 */
public class AdminUsersController {

    @FXML
    private ListView<UserSummaryResponse> usersListView;

    @FXML
    private Label errorLabel;

    private final AdminService adminService = new AdminService();

    @FXML
    public void initialize() {
        usersListView.setCellFactory(list -> new UserRowCell());
        loadUsers();
    }

    @FXML
    private void handleRefresh() {
        loadUsers();
    }

    @FXML
    private void handleBack() {
        SceneManager.switchTo("/com/secondhand/frontend/view/admin-home-view.fxml", "Admin Dashboard");
    }

    private void loadUsers() {
        hideError();

        Task<List<UserSummaryResponse>> task = new Task<>() {
            @Override
            protected List<UserSummaryResponse> call() throws Exception {
                return adminService.getAllUsers();
            }
        };

        task.setOnSucceeded(event -> usersListView.getItems().setAll(task.getValue()));

        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            if (ex instanceof ApiException apiEx) {
                showError(apiEx.getMessage());
            } else {
                showError("Could not connect to the server. Make sure the backend is running.");
            }
        });

        new Thread(task).start();
    }

    private void toggleBlock(UserSummaryResponse user) {
        hideError();

        Task<UserSummaryResponse> task = new Task<>() {
            @Override
            protected UserSummaryResponse call() throws Exception {
                if ("BLOCKED".equals(user.getStatus())) {
                    return adminService.unblockUser(user.getId());
                } else {
                    return adminService.blockUser(user.getId());
                }
            }
        };

        task.setOnSucceeded(event -> loadUsers());

        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            if (ex instanceof ApiException apiEx) {
                showError(apiEx.getMessage());
            } else {
                showError("Could not connect to the server.");
            }
        });

        new Thread(task).start();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
    }

    /**
     * Custom row: user info on the left, a Block/Unblock button on the right.
     */
    private class UserRowCell extends ListCell<UserSummaryResponse> {

        private final Label infoLabel = new Label();
        private final Button actionButton = new Button();
        private final HBox root = new HBox(12);

        UserRowCell() {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(6));
            root.getChildren().addAll(infoLabel, spacer, actionButton);

            actionButton.setOnAction(event -> {
                UserSummaryResponse user = getItem();
                if (user != null) {
                    toggleBlock(user);
                }
            });
        }

        @Override
        protected void updateItem(UserSummaryResponse user, boolean empty) {
            super.updateItem(user, empty);

            if (empty || user == null) {
                setGraphic(null);
                return;
            }

            String statusColor = "BLOCKED".equals(user.getStatus()) ? "red" : "green";
            infoLabel.setText(user.getUsername() + " — " + user.getFullName()
                    + "  |  " + user.getRole() + "  |  " + user.getStatus());
            infoLabel.setStyle("-fx-text-fill: " + statusColor + ";");

            actionButton.setText("BLOCKED".equals(user.getStatus()) ? "Unblock" : "Block");

            setGraphic(root);
        }
    }
}
