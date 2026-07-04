package com.secondhand.frontend.service;

import com.secondhand.frontend.service.dto.AuthResponse;
import com.secondhand.frontend.service.dto.LoginRequest;
import com.secondhand.frontend.service.dto.RegisterRequest;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Client-side service for authentication endpoints.
 * <p>
 * Wraps the /api/auth/register and /api/auth/login endpoints.
 * All methods perform actual HTTP calls to the backend — no direct storage access.
 * This follows the required client-server architecture.
 * </p>
 */
public class AuthService {

    public AuthResponse register(RegisterRequest requestBody) throws ApiException, Exception {
        String json = ApiClient.mapper().writeValueAsString(requestBody);

        HttpRequest request = ApiClient.requestBuilder("/auth/register")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(), AuthResponse.class);
    }

    public AuthResponse login(LoginRequest requestBody) throws ApiException, Exception {
        String json = ApiClient.mapper().writeValueAsString(requestBody);

        HttpRequest request = ApiClient.requestBuilder("/auth/login")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(), AuthResponse.class);
    }
}
