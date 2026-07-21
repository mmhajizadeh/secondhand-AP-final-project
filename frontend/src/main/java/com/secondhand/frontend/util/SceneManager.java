package com.secondhand.frontend.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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

    public static void switchTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            if (primaryStage != null) {
                double width = 950;
                double height = 650;

                if (primaryStage.getScene() != null) {
                    width = primaryStage.getScene().getWidth();
                    height = primaryStage.getScene().getHeight();
                }

                Scene scene = new Scene(root,  width, height);
                primaryStage.setScene(scene);
                primaryStage.setTitle(title);
                primaryStage.show();
            }
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
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
