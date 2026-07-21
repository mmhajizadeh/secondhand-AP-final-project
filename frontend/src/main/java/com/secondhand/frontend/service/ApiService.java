package com.secondhand.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.model.Category;
import com.secondhand.frontend.model.City;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Service class for handling HTTP communication with the backend API.
 */
public class ApiService {
    private static final String BASE_URL = "http://localhost:8080/api/advertisements";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Fetches a list of active advertisements from the backend.
     *
     * @return A list of {@link Advertisement} objects.
     * @throws Exception if the network request fails or JSON parsing errors occur.
     */
    public static List<Advertisement> getActiveAds() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/active"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("HTTP Status Code: " + response.statusCode());
        System.out.println("Response Body: '" + response.body() + "'");

        if (response.statusCode() != 200) {
            throw new RuntimeException("Server returned error code: " +  response.statusCode() + " with body: " + response.body());
        }

        return mapper.readValue(response.body(), new TypeReference<List<Advertisement>>() {});
    }

    /**
     * Fetches all available cities from the backend.
     *
     * @return A list of {@link City} objects.
     * @throws Exception if the network request fails or JSON parsing errors occur.
     */
    public static List<City> getAllCities() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/cities/all"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if  (response.statusCode() != 200) {
            throw new RuntimeException("Error fetching cities: " +  response.statusCode());
        }

        return mapper.readValue(response.body(), new TypeReference<List<City>>() {});
    }

    /**
     * Fetches all advertisement categories from the backend.
     *
     * @return A list of {@link Category} objects.
     * @throws Exception if the network request fails or JSON parsing errors occur.
     */
    public static List<Category> getAllCategories() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/categories/all"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error fetching categories: " +  response.statusCode());
        }

        return mapper.readValue(response.body(), new TypeReference<List<Category>>() {});
    }
}
