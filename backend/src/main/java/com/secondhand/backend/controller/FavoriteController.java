package com.secondhand.backend.controller;

import com.secondhand.backend.entity.Advertisement;
import com.secondhand.backend.entity.Favorite;
import com.secondhand.backend.service.FavoriteService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing user favorite advertisements.
 * <p>
 * Provides endpoints for authenticated users to add, remove, and retrieve
 * their favorite ads. Base path is "/api/favorites".
 * </p>

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
     * Retrieves all favorite advertisements for the currently authenticated user.
     * Endpoint: GET /api/favorites
     *
     * @param authentication Security context containing authenticated user credentials.
     * @return List of favorite {@link Advertisement} objects.
     */
    @GetMapping
    public List<Advertisement> getMyFavorites(Authentication authentication) {
        return favoriteService.getUserFavoriteAds(authentication.getName());
    }

    /**
     * Adds an advertisement to the authenticated user's favorites by ad ID.
     * Endpoint: POST /api/favorites/{adId}
     *
     * @param adId           The ID of the advertisement to favorite.
     * @param authentication Security context containing authenticated user credentials.
     * @return The saved {@link Favorite} relation entity.
     */
    @PostMapping("/{adId}")
    public Favorite addFavorite(@PathVariable Long adId, Authentication authentication) {
        return favoriteService.addFavorite(adId, authentication.getName());
    }

    /**
     * Removes an advertisement from the authenticated user's favorites by ad ID.
     * Endpoint: DELETE /api/favorites/{adId}
     *
     * @param adId           The ID of the advertisement to remove from favorites.
     * @param authentication Security context containing authenticated user credentials.
     */
    @DeleteMapping("/{adId}")
    public void removeFavorite(@PathVariable Long adId, Authentication authentication) {
        favoriteService.removeFavorite(adId, authentication.getName());
    }
}