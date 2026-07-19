package com.secondhand.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private FlowPane adsContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadAds();
    }

    private void loadAds() {
        try {
            List<Advertisement> ads = ApiService.getActiveAds();
            DecimalFormat priceFormat = new DecimalFormat("#,###");

            for (Advertisement ad : ads) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ad-card.fxml"));
                HBox cardBox = fxmlLoader.load();

                AdCardController cardController = fxmlLoader.getController();

                String cityName = (ad.getCity() != null) ? ad.getCity().getName() : "نامشخص";
                String categoryName = (ad.getCategory() != null) ? ad.getCategory().getName() : "بدون دسته";

                cardController.setAdData(
                        ad.getTitle(),
                        priceFormat.format(ad.getPrice()) + " تومان",
                        categoryName + " | " + cityName + " | دقایقی پیش",
                        "image-url"
                );

                adsContainer.getChildren().add(cardBox);
            }
        } catch (Exception e) {
            System.err.println("Error fetching ads from server: " +  e.getMessage());
            e.printStackTrace();
        }
    }
}
