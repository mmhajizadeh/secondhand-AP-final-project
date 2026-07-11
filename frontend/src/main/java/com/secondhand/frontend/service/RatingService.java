package com.secondhand.frontend.service;

import com.secondhand.frontend.service.dto.RatingRequest;
import com.secondhand.frontend.service.dto.RatingResponse;
import com.secondhand.frontend.service.dto.SellerRatingSummary;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
/**
 * Wraps the / api / ratings endpoints
 */
public class RatingService {

    public RatingResponse submitRating(RatingRequest requestBody) throws ApiException, Exception {
        String json = ApiClient.mapper().writeValueAsString(requestBody);

        HttpRequest request = ApiClient.requestBuilder("/ratings")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);
        return ApiClient.mapper().readValue(response.body(), RatingResponse.class);
    }

    public SellerRatingSummary getSellerRatingSummary(Long sellerId) throws ApiException, Exception {
        HttpRequest request = ApiClient.requestBuilder("/ratings/seller/" + sellerId)
                .GET()
                .build();
        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(), SellerRatingSummary.class);
    }
}
