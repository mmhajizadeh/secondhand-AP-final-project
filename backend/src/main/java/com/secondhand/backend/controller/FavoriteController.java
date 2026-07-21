package com.secondhand.backend.controller;

import com.secondhand.backend.model.Favorite;
import com.secondhand.backend.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing user favorite advertisements.
 * <p>
 * Provides endpoints for users to add, remove, and retrieve
 * their favorite ads. Base path is "/api/favorites".
 * </p>
 *
 * @author mmhajizadeh
 * @since 1.0
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * Retrieves all favorite advertisements for a specific user.
     *
     * @param userId The ID of the user passed in the URL path.
     * @return A list of {@link Favorite} objects belonging to the user.
     * Endpoint: GET /api/favorites/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public List<Favorite> getUserFavorites(@PathVariable Long userId) {
        return favoriteService.getUserFavorites(userId);
    }

    /**
     * Adds an advertisement to the user's favorites.
     *
     * @param favorite The favorite data sent as a JSON payload.
     * @return The saved {@link Favorite} object.
     * Endpoint: POST /api/favorites
     */
    @PostMapping
    public Favorite addFavorite(@RequestBody Favorite favorite) {
        return favoriteService.addFavorite(favorite);
    }

    /**
     * Removes an advertisement from the user's favorites.
     *
     * @param id The ID of the favorite record to delete.
     * Endpoint: DELETE /api/favorites/{id}
     */
    @DeleteMapping("/{id}")
    public void removeFavorite(@PathVariable Long id) {
        favoriteService.removeFavorite(id);
    }
}