package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.Category;
import com.secondhand.frontend.model.City;
import com.secondhand.frontend.service.ApiService;
import com.secondhand.frontend.util.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewAdController implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ComboBox<City> cityComboBox;
    @FXML private TextArea descriptionArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDropdowns();
    }

    private void loadDropdowns() {
        try {
            List<City> cities = ApiService.getAllCities();
            List<Category> categories = ApiService.getAllCategories();

            cityComboBox.getItems().addAll(cities);
            categoryComboBox.getItems().addAll(categories);

            cityComboBox.setConverter(new StringConverter<City>() {
               @Override
               public String toString(City city) {
                   return (city == null) ? null : city.getName();
               }
               @Override
               public City fromString(String string) {
                   return null;
               }
            });

            categoryComboBox.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return  (category == null) ? null : category.getName();
                }

                @Override
                public Category fromString(String s) {
                    return null;
                }
            });
        } catch (Exception e) {
            showError("Failed to load categories and cities.");
        }
    }

    @FXML
    private void handleSubmit() {
        String title = titleField.getText();
        String desc = descriptionArea.getText();
        String priceText = priceField.getText();
        Category selectedCategory = categoryComboBox.getValue();
        City selectedCity = cityComboBox.getValue();

        if (title == null || title.isBlank() || desc == null || desc.isBlank() || priceText == null || priceText.isBlank() || selectedCity == null || selectedCategory == null) {
            showError("Please fill out all fields.");
            return;
        }

        try {
            Long price = Long.parseLong(priceText.trim().replace(",", ""));

            // send to backend
            ApiService.createAd(title, desc, price, selectedCategory.getId(), selectedCity.getId());

            showSuccess("Advertisement submitted successfully! It is now pending admin approval.");
            handleCancel(); // Back to Main View
        } catch (NumberFormatException e) {
            showError("Please enter a valid price number.");
        } catch (Exception e) {
            showError("Error submitting ad: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        SceneManager.switchTo("/com/secondhand/frontend/view/main-view.fxml", "Second Hand Market");
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showSuccess(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
