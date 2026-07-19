package com.secondhand.backend.controller;

import com.secondhand.backend.dto.RatingRequest;
import com.secondhand.backend.dto.RatingResponse;
import com.secondhand.backend.dto.SellerRatingSummary;
import com.secondhand.backend.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Rating endpoints.
 * POST / api / ratings requires authentication ( the rater must be logged in ).
 * GET / api / ratings / seller /{sellerId} is used to show a sellers rating
 * summary on their profile / ad detail page.
 */
@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<RatingResponse> submitRating(@Valid @RequestBody RatingRequest request,
                                                         Authentication authentication) {
        String raterUsername = authentication.getName();
        RatingResponse response = ratingService.submitRating(raterUsername, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<SellerRatingSummary> getSellerRatingSummary(@PathVariable Long sellerId) {
        SellerRatingSummary summary = ratingService.getSellerRatingSummary(sellerId);
        return ResponseEntity.ok(summary);
    }
}
