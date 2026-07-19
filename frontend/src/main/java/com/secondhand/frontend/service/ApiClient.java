package com.secondhand.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.frontend.service.dto.ErrorResponse;
import com.secondhand.frontend.session.SessionManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Central HTTP client for all backend communication.
 * <p>
 * Every *Service class uses this client to send requests to the Spring Boot backend.
 * The frontend never accesses the database directly — all requests go through real HTTP calls,
 * following the client-server architecture required by the project.
 * </p>
 * <p>
 * If a valid session exists, the JWT token is automatically added
 * to the Authorization header of every outgoing request.
 * </p>
 */
public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ApiClient() {
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }

    public static HttpRequest.Builder requestBuilder(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json");

        String token = SessionManager.getInstance().getToken();
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }

        return builder;
    }

    public static HttpResponse<String> send(HttpRequest request) throws Exception {
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Checks the HTTP status code and throws an ApiException with the
     * backends error message if the request was not successful (2xx).
     */
    public static void throwIfError(HttpResponse<String> response) throws ApiException {
        int status = response.statusCode();
        if (status >= 200 && status < 300) {
            return;
        }

        String message = "Request failed with status " + status;
        try {
            ErrorResponse error = MAPPER.readValue(response.body(), ErrorResponse.class);
            if (error.getMessage() != null && !error.getMessage().isBlank()) {
                message = error.getMessage();
            }
        } catch (Exception ignored) {
        // Body wasnt a parseable ErrorResponse; fall back to the generic message above
        }

        throw new ApiException(message, status);
    }
}
