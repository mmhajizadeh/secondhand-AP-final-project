package com.secondhand.backend.repository;

import com.secondhand.backend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Favorite} entities.
 * <p>
 * This interface provides automated CRUD operations and custom query methods
 * for user favorite advertisements using Spring Data JPA.
 * </p>
 *
 * @author mmhajizadeh
 * @since 1.0
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    /**
     * Finds all favorite records for a specific user ID.
     *
     * @param userId The ID of the user.
     * @return A list of {@link Favorite} objects.
     */
    List<Favorite> findByUserId(Long userId);

    /**
     * Finds all favorite records associated with a specific username.
     *
     * @param username The username of the account owner.
     * @return A list of {@link Favorite} entities.
     */
    List<Favorite> findByUserUsername(String username);

    /**
     * Finds a single favorite relation between a user and a specific advertisement.
     *
     * @param username The username of the user.
     * @param advertisementId The ID of the advertisement.
     * @return An {@link Optional} containing the {@link Favorite} entity if found.
     */
    Optional<Favorite> findByUserUsernameAndAdvertisementId(String username, Long advertisementId);
}