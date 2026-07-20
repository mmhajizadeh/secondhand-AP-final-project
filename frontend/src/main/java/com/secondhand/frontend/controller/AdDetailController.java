package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.util.NavigationContext;
import com.secondhand.frontend.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class AdDetailController implements Initializable {

    private static Advertisement currentAd;

    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private Label categoryCityLabel;
    @FXML private Label ownerLabel;
    @FXML private Text descriptionText;

    public static void setSelectedAd(Advertisement ad) {
        currentAd = ad;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (currentAd != null) {
            DecimalFormat priceFormat = new DecimalFormat("#,###");
            titleLabel.setText(currentAd.getTitle());
            priceLabel.setText(priceFormat.format(currentAd.getPrice()) + " تومان");

            String cityName = (currentAd.getCity() != null) ? currentAd.getCity().getName() : "نامشخص";
            String catName = (currentAd.getCategory() != null) ? currentAd.getCategory().getName() : "بدون دسته";
            categoryCityLabel.setText("دسته‌بندی: " + catName + " | شهر: " + cityName);
        }
    }

    @FXML
    public void onBackAction() {
        SceneManager.switchTo("/com/secondhand/frontend/view/main-view.fxml", "Second Hand Market");
    }

    @FXML
    private void handleChatWithSeller() {
        if (currentAd != null) {
            NavigationContext.setTargetAdvertisementId(currentAd.getId());
            NavigationContext.setTargetSellerUsername(currentAd.getOwnerUsername());
            SceneManager.switchTo("/com/secondhand/frontend/view/start-conversation-view.fxml", "Start Conversation");
        }
    }

    @FXML
    private void handleRateSeller() {
        if (currentAd != null) {
            NavigationContext.setTargetAdvertisementId(currentAd.getId());
            SceneManager.switchTo("/com/secondhand/frontend/view/rate-seller-view.fxml", "Rate Seller");
        }
    }
}
