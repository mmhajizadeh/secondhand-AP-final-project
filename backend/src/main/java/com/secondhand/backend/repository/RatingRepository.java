package com.secondhand.backend.repository;

import com.secondhand.backend.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    boolean existsByRaterIdAndAdvertisementId(Long raterId, Long advertisementId);

    Optional<Rating> findByRaterIdAndAdvertisementId(Long raterId, Long advertisementId);

    List<Rating> findBySellerIdOrderByCreatedAtDesc(Long sellerId);

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.sellerId = :sellerId")
    Double findAverageScoreBySellerId(@Param("sellerId") Long sellerId);

    long countBySellerId(Long sellerId);
}
