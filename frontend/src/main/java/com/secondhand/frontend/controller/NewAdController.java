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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

public class NewAdController implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ComboBox<City> cityComboBox;
    @FXML private TextArea descriptionArea;
    @FXML private Label imageCountLabel;

    private final List<String> base64Images = new ArrayList<>();

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
    private void handleSelectImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Ad Images");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        List<File> files = fileChooser.showOpenMultipleDialog(titleField.getScene().getWindow());

        if (files != null) {
            if (files.size() > 3) {
                showError("You can only select up to 3 images. The first 3 will be used.");
                files = files.subList(0, 3);
            } else {
                base64Images.clear(); // پاک کردن انتخاب‌های قبلی
            }

            for (File file : files) {
                try {
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    String base64 = Base64.getEncoder().encodeToString(fileContent);
                    base64Images.add(base64);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (imageCountLabel != null) {
                imageCountLabel.setText(base64Images.size() + "/3 selected");
            }
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

            ApiService.createAd(title, desc, price, selectedCategory.getId(), selectedCity.getId(), base64Images);

            showSuccess("Advertisement submitted successfully! It is now pending admin approval.");
            handleCancel();
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