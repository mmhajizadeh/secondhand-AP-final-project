package com.secondhand.frontend.controller;

import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.model.Category;
import com.secondhand.frontend.model.City;
import com.secondhand.frontend.service.ApiService;
import com.secondhand.frontend.util.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

/**
 * Controller for creating new advertisements or editing existing ones.
 */
public class NewAdController implements Initializable {

    private static Advertisement adToEdit = null;

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ComboBox<City> cityComboBox;
    @FXML private TextArea descriptionArea;
    @FXML private Label imageCountLabel;
    @FXML private Button submitButton;

    private final List<String> base64Images = new ArrayList<>();

    /**
     * Sets the advertisement to be edited. Pass null to switch back to creation mode.
     */
    public static void setAdToEdit(Advertisement ad) {
        adToEdit = ad;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDropdowns();
        setupPriceValidation();

        if (adToEdit != null) {
            populateFieldsForEdit();
        }
    }

    /**
     * Restricts the price text field to accept only numeric values.
     */
    private void setupPriceValidation() {
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                priceField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void populateFieldsForEdit() {
        titleField.setText(adToEdit.getTitle());
        priceField.setText(String.valueOf(adToEdit.getPrice()));
        descriptionArea.setText(adToEdit.getDescription());
        if (submitButton != null) {
            submitButton.setText("Update Ad");
        }
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

            if (adToEdit != null) {
                cities.stream().filter(c -> c.getId().equals(adToEdit.getCity().getId())).findFirst().ifPresent(cityComboBox::setValue);
                categories.stream().filter(c -> c.getId().equals(adToEdit.getCategory().getId())).findFirst().ifPresent(categoryComboBox::setValue);
            }

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
            for (File file : files) {
                if (base64Images.size() >= 3) {
                    showError("You have reached the maximum limit of 3 images.");
                    break;
                }

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

            if (adToEdit == null) {
                ApiService.createAd(title, desc, price, selectedCategory.getId(), selectedCity.getId(), base64Images);
                showSuccess("Advertisement submitted successfully! It is now pending admin approval.");
            } else {
                ApiService.updateAd(adToEdit.getId(), title, desc, price, selectedCategory.getId(), selectedCity.getId(), base64Images);
                showSuccess("Advertisement updated successfully! It is now pending admin re-approval.");
                adToEdit = null;
            }

            handleCancel();
        } catch (Exception e) {
            showError("Error submitting ad: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        adToEdit = null;
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