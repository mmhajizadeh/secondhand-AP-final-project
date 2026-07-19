package com.secondhand.frontend;

import com.secondhand.frontend.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main JavaFX entry point. Starts the application on the login screen.
 */
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setPrimaryStage(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("/com/secondhand/frontend/view/login-view.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 420, 420);
        stage.setTitle("Second Hand Market");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
