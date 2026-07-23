package com.secondhand.backend.service;

import com.secondhand.backend.entity.Advertisement;
import com.secondhand.backend.entity.Favorite;
import com.secondhand.backend.entity.User;
import com.secondhand.backend.repository.AdvertisementRepository;
import com.secondhand.backend.repository.FavoriteRepository;
import com.secondhand.backend.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           UserRepository userRepository,
                           AdvertisementRepository advertisementRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
    }

    /**
     * Retrieves all favorite advertisements for a user based on their username.
     *
     * @param username The username of the requesting user.
     * @return A list of favorited {@link Advertisement} entities.
     */
    public List<Advertisement> getUserFavoriteAds(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return favoriteRepository.findByUserId(user.getId())
                .stream()
                .map(Favorite::getAdvertisement)
                .toList();
    }

    /**
     * Adds an advertisement to a user's favorites by advertisement ID and username.
     *
     * @param adId     The ID of the advertisement to favorite.
     * @param username The username of the user favoriting the ad.
     * @return The saved {@link Favorite} record.
     */
    public Favorite addFavorite(Long adId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return favoriteRepository.findByUserIdAndAdvertisementId(user.getId(), adId)
                .orElseGet(() -> {
                    Advertisement ad = advertisementRepository.findById(adId)
                            .orElseThrow(() -> new RuntimeException("Advertisement not found with ID: " + adId));

                    Favorite favorite = new Favorite();
                    favorite.setUserId(user.getId());
                    favorite.setAdvertisement(ad);
                    return favoriteRepository.save(favorite);
                });
    }

    /**
     * Removes an advertisement from a user's favorite list by advertisement ID and username.
     *
     * @param adId     The ID of the advertisement to remove.
     * @param username The username of the user.
     */
    public void removeFavorite(Long adId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        favoriteRepository.findByUserIdAndAdvertisementId(user.getId(), adId)
                .ifPresent(favoriteRepository::delete);
    }
}