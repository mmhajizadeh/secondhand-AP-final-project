package com.secondhand.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.frontend.model.Advertisement;
import com.secondhand.frontend.model.Category;
import com.secondhand.frontend.model.City;
import com.secondhand.frontend.session.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    /**
     * Fetches a list of pending advertisements (Admin only).
     */
    public static List<Advertisement> getPendingAds() throws Exception {
        String token = SessionManager.getInstance().getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/pending"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error fetching pending ads: " +  response.statusCode());
        }

        return mapper.readValue(response.body(), new TypeReference<List<Advertisement>>() {});
    }

    /**
     * Approves a pending advertisement (Admin only).
     */
    public static void approveAd(Long adId) throws Exception {
        String token = SessionManager.getInstance().getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + adId + "/approve"))
                .header("Authorization", "Bearer" + token)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to approved ad: " +  response.statusCode());
        }
    }

    /**
     * Rejects a pending advertisement (Admin only).
     */
    public static void rejectAd(Long adId) throws Exception {
        String token = SessionManager.getInstance().getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + adId + "/reject"))
                .header("Authorization", "Bearer" + token)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to reject ad: " +  response.statusCode());
        }
    }

    /**
     * Sends a request to create a new advertisement in the system.
     * <p>
     * Constructs a JSON payload with the ad details and sends a POST request
     * to the backend. Requires a valid JWT token stored in {@link SessionManager}.
     * </p>
     *
     * @param title       The title of the advertisement.
     * @param description Detailed text describing the item.
     * @param price       Price of the item in Tomans.
     * @param categoryId  The unique database ID of the selected category.
     * @param cityId      The unique database ID of the selected city.
     * @throws Exception if the network request fails, unauthorized, or backend returns an error.
     */
    public static void createAd(String title, String description, Long price, Long categoryId, Long cityId) throws Exception {
        String token = SessionManager.getInstance().getToken();

        Map<String, Object> categoryMap = new HashMap<>();
        categoryMap.put("id", categoryId);

        Map<String, Object> cityMap = new HashMap<>();
        cityMap.put("id", cityId);

        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("description", description);
        body.put("price", price);
        body.put("category", categoryMap);
        body.put("city", cityMap);
        body.put("status", "PENDING");

        String jsonBody = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to create ad: " +  response.statusCode());
        }
    }

    /**
     * Fetches advertisements strictly owned by the currently logged-in user.
     * The backend securely identifies the user via the provided JWT token.
     *
     * @return A list of {@link Advertisement} objects belonging to the user.
     * @throws Exception if the network request fails, is unauthorized, or JSON parsing errors occur.
     */
    public static List<Advertisement> getMyAds() throws Exception {
        String token = SessionManager.getInstance().getToken();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/my"))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error fetching user  ads: " +  response.statusCode());
        }

        return mapper.readValue(response.body(), new TypeReference<List<Advertisement>>() {});
    }

    /**
     * Updates the status of a specific advertisement to SOLD.
     * This action indicates that the item has been successfully traded and is no longer available.
     *
     * @param adId The unique database identifier of the advertisement.
     * @throws Exception if the network request fails, is unauthorized, or the backend returns an error.
     */
    public static void markAdAsSold(Long adId) throws Exception {
        String token = SessionManager.getInstance().getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + adId + "/status?status=SOLD"))
                .header("Authorization", "Bearer " + token)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to mark ad as sold: " +  response.statusCode());
        }
    }

    /**
     * Permanently deletes a specific advertisement from the system.
     *
     * @param adId The unique database identifier of the advertisement to be deleted.
     * @throws Exception if the network request fails, is unauthorized, or the backend returns an error.
     */
    public static void deleteAd(Long adId) throws Exception {
        String token = SessionManager.getInstance().getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + adId))
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to delete ad: " + response.statusCode());
        }
    }
}
