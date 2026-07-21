package com.secondhand.backend.service;

import com.secondhand.backend.entity.Favorite;
import com.secondhand.backend.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing user {@link Favorite}s.
 * <p>
 * Handles business logic for adding, retrieving, and removing
 * advertisements from a user's favorite list.
 * </p>
 *
 * @author mmhajizadeh
 * @since 1.0
 */
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * Retrieves all favorite advertisements specific to a user.
     *
     * @param userId The ID of the user requesting their favorites.
     * @return A list of {@link Favorite} entities.
     */
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    /**
     * Adds an advertisement to the user's favorite list.
     *
     * @param favorite The {@link Favorite} entity to be saved.
     * @return The saved {@link Favorite} entity.
     */
    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    /**
     * Removes an advertisement from the user's favorite list.
     *
     * @param favoriteId The unique ID of the favorite record to be removed.
     */
    public void removeFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
