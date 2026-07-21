package com.secondhand.backend.repository;

import com.secondhand.backend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * Finds all favorite advertisements for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of {@link Favorite} objects belonging to the user.
     */
    List<Favorite> findByUserId(Long userId);
}
