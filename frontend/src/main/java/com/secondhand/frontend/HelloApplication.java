package com.secondhand.frontend;

import com.secondhand.frontend.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main JavaFX entry point. Starts the application on the main view screen.
 */
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setPrimaryStage(stage);

        SceneManager.switchTo("/com/secondhand/frontend/view/main-view.fxml", "Second Hand Market");
    }

    public static void main(String[] args) {
        launch();
    }
}