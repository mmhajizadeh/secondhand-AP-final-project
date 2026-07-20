package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.model.Category;
import com.secondhand.frontend.model.City;
import com.secondhand.frontend.service.ApiService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller class for the main dashboard view of the application.
 * <p>
 * Handles fetching, displaying, and hybrid filtering (by text, city, category, and price range)
 * of active advertisements from the backend API.
 * </p>
 */
public class MainController implements Initializable {
    @FXML private FlowPane adsContainer;
    @FXML private ComboBox<City> cityComboBox;
    @FXML private ComboBox<Category> categoryComboBox;

    @FXML private TextField searchField;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;

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
            displayAds(ads);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void displayAds(List<Advertisement> ads) {
        adsContainer.getChildren().clear();
        DecimalFormat priceFormat = new DecimalFormat("#,###");

        try {
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
            e.printStackTrace();
        }
    }

    /**
     * Executes in-memory combined filtering on active advertisements when the Search button is clicked.
     * Filters by text search, selected category, selected city, and price range.
     */
    @FXML
    public void onSearchAction() {
        try {
            List<Advertisement> allAds = ApiService.getActiveAds();

            // filtering
            List<Advertisement> filteredAds = allAds.stream().filter(ad -> {
                boolean match = true;

                if (searchField.getText() != null && !searchField.getText().trim().isEmpty()) {
                    String keyword = searchField.getText().toLowerCase();
                    boolean inTitle = ad.getTitle() != null && ad.getTitle().toLowerCase().contains(keyword);
                    boolean inDesc =  ad.getDescription() != null && ad.getDescription().toLowerCase().contains(keyword);
                    if (!inTitle && !inDesc) match = false;
                }

                // category filtering
                if (categoryComboBox.getValue() != null) {
                    if (ad.getCategory() == null || !ad.getCategory().getId().equals(categoryComboBox.getValue().getId())) {
                        match = false;
                    }
                }

                // city filtering
                if (cityComboBox.getValue() != null) {
                    if (ad.getCity() == null || !ad.getCity().getId().equals(cityComboBox.getValue().getId())) {
                        match = false;
                    }
                }

                // price filtering
                if (minPriceField.getText() != null && !minPriceField.getText().trim().isEmpty()) {
                    try {
                        Long minPrice = Long.parseLong(minPriceField.getText().replace(",", ""));
                        if (ad.getPrice() < minPrice) match = false;
                    } catch (NumberFormatException ignored) {}
                }

                if (maxPriceField.getText() != null && !maxPriceField.getText().trim().isEmpty()) {
                    try {
                        Long maxPrice = Long.parseLong(maxPriceField.getText().replace(",", ""));
                        if (ad.getPrice() > maxPrice) match = false;
                    } catch (NumberFormatException ignored) {}
                }

                return match;
            }).collect(Collectors.toList());

            displayAds(filteredAds);

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void showErrorAlert(String errorMessage) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("خطای ارتباط با سرور");
            alert.setHeaderText("مشکلی در دریافت اطلاعات پیش آمد.");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        });
    }
}
