package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.model.Category;
import com.secondhand.frontend.model.City;
import com.secondhand.frontend.service.ApiService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.rmi.ServerError;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private FlowPane adsContainer;

    @FXML
    private ComboBox<City> cityComboBox;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadFilters();
        loadAds();
    }

    private void loadFilters() {
        try {
            List<City> cities = ApiService.getAllCities();
            List<Category> categories = ApiService.getAllCategories();

            cityComboBox.getItems().addAll(cities);
            categoryComboBox.getItems().addAll(categories);
        } catch (Exception e) {
            System.err.println("Error loading filters: " + e.getMessage());
        }
    }

    private void loadAds() {
        try {
            List<Advertisement> ads = ApiService.getActiveAds();
            DecimalFormat priceFormat = new DecimalFormat("#,###");

            for (Advertisement ad : ads) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/secondhand/frontend/view/ad-card.fxml"));
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

            javafx.application.Platform.runLater(() -> {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("خطای ارتباط با سرور");
                alert.setHeaderText("متاسفانه نتوانستیم آگهی‌ها را دریافت کنیم.");
                alert.setContentText("لطفاً اتصال اینترنت خود را بررسی کنید یا دقایقی دیگر مجدداً تلاش نمایید.\nجزئیات خطا: " + e.getMessage());
                alert.showAndWait();
            });
        }
    }
}
