package com.secondhand.frontend.service;

import com.secondhand.frontend.service.dto.AdminStatsResponse;
import com.secondhand.frontend.service.dto.UserSummaryResponse;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Wraps the / api / admin / users endpoints. Only usable by a logged in ADMIN
 * (the backend enforces this the UI should only show this screen to admins)
 */
public class AdminService {

    public List<UserSummaryResponse> getAllUsers() throws ApiException, Exception {
        HttpRequest request = ApiClient.requestBuilder("/admin/users")
                .GET()
                .build();

        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(),
                ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, UserSummaryResponse.class));
    }

    public AdminStatsResponse getAdminStats() throws ApiException, Exception {
        HttpRequest request = ApiClient.requestBuilder("/admin/users/stats")
                .GET()
                .build();

        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(), AdminStatsResponse.class);
    }

    public UserSummaryResponse blockUser(Long userId) throws ApiException, Exception {
        HttpRequest request = ApiClient.requestBuilder("/admin/users/" + userId + "/block")
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(), UserSummaryResponse.class);
    }

    public UserSummaryResponse unblockUser(Long userId) throws ApiException, Exception {
        HttpRequest request = ApiClient.requestBuilder("/admin/users/" + userId + "/unblock")
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(), UserSummaryResponse.class);
    }
}
