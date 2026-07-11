package com.secondhand.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private FlowPane adsContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadAds();
    }

    private void loadAds() {
        for (int i = 1; i <= 6; i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ad-card.fxml"));
                HBox cardBox = fxmlLoader.load();

                AdCardController cardController = fxmlLoader.getController();
                cardController.setAdData(
                        "دوچرخه کوهستانی مدل " + i,
                        "5,000,000 تومان",
                        "تهران | دقایقی پیش",
                        "dummy_image_url"
                );

                adsContainer.getChildren().add(cardBox);

            } catch (IOException e) {
                System.err.println("Error loading ad card: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
