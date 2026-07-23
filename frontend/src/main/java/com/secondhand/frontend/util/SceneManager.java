package com.secondhand.frontend.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Small helper to switch the content of the main application window between
 * different FXML screens (login, register, main listings, chat, admin, ...).
 */
public class SceneManager {

    private static Stage primaryStage;

    private SceneManager() {
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Retrieves the primary application stage.
     *
     * @return The primary {@link Stage} of the application.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void switchTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            if (primaryStage.getScene() == null) {
                Scene scene = new Scene(root, 1200, 700);
                applyCss(scene);
                primaryStage.setScene(scene);
            } else {
                double currentWidth = primaryStage.getScene().getWidth();
                double currentHeight = primaryStage.getScene().getHeight();

                Scene scene = new Scene(root, currentWidth, currentHeight);
                applyCss(scene);
                primaryStage.setScene(scene);
            }

            primaryStage.setTitle(title);
            primaryStage.setMinWidth(1200);
            primaryStage.setMinHeight(700);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load screen: " + fxmlPath, e);
        }
    }

    public static void showAsPopup(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);

            Scene scene = new Scene(root);
            applyCss(scene);

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to attach global style.css if present in resources.
     */
    private static void applyCss(Scene scene) {
        URL cssUrl = SceneManager.class.getResource("/com/secondhand/frontend/css/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
    }
}
