package com.secondhand.frontend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Service class for handling HTTP communication with the backend API.
 */
public class ApiService {
    private static final String BASE_URL = "http://localhost:8080/api/ads";
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

        return mapper.readValue(response.body(), new TypeReference<List<Advertisement>>() {});
    }
}
