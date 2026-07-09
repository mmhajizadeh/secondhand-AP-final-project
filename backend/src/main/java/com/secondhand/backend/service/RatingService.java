package com.secondhand.backend.service;

import com.secondhand.backend.dto.RatingRequest;
import com.secondhand.backend.dto.RatingResponse;
import com.secondhand.backend.dto.SellerRatingSummary;
import com.secondhand.backend.entity.Rating;
import com.secondhand.backend.entity.User;
import com.secondhand.backend.exception.DuplicateResourceException;
import com.secondhand.backend.exception.ForbiddenOperationException;
import com.secondhand.backend.exception.ResourceNotFoundException;
import com.secondhand.backend.repository.RatingRepository;
import com.secondhand.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Core rating logic.
 * Per the spec:
 * - Score must be 1-5 (enforced by @Min/@Max on the DTO, double-checked here).
 * - A user cannot rate themselves.
 * - A user can only rate the same seller for the same advertisement once.
 * - The average and count must be computable for any seller.
 */
@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    public RatingService(RatingRepository ratingRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    public RatingResponse submitRating(String raterUsername, RatingRequest request) {
        User rater = userRepository.findByUsername(raterUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (rater.getId().equals(request.getSellerId())) {
            throw new ForbiddenOperationException("You cannot rate yourself");
        }

        userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        if (request.getScore() < 1 || request.getScore() > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }

        if (ratingRepository.existsByRaterIdAndAdvertisementId(rater.getId(), request.getAdvertisementId())) {
            throw new DuplicateResourceException("You have already rated this seller for this advertisement");
        }

        Rating rating = new Rating(
                rater.getId(),
                request.getSellerId(),
                request.getAdvertisementId(),
                request.getScore(),
                request.getComment()
        );
        Rating saved = ratingRepository.save(rating);
        return toResponse(saved, rater.getUsername());
    }

    public SellerRatingSummary getSellerRatingSummary(Long sellerId) {
        userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        List<Rating> ratings = ratingRepository.findBySellerIdOrderByCreatedAtDesc(sellerId);
        long total = ratings.size();

        Double average = ratingRepository.findAverageScoreBySellerId(sellerId);
        double averageScore = average != null ? Math.round(average * 10.0) / 10.0 : 0.0;

        List<RatingResponse> responses = ratings.stream()
                .map(r -> toResponse(r, usernameOf(r.getRaterId())))
                .toList();

        return new SellerRatingSummary(sellerId, averageScore, total, responses);
    }

    private String usernameOf(Long userId) {
        return userRepository.findById(userId).map(User::getUsername).orElse("unknown");
    }

    private RatingResponse toResponse(Rating rating, String raterUsername) {
        return new RatingResponse(
                rating.getId(),
                rating.getRaterId(),
                raterUsername,
                rating.getSellerId(),
                rating.getAdvertisementId(),
                rating.getScore(),
                rating.getComment(),
                rating.getCreatedAt()
        );
    }
}
