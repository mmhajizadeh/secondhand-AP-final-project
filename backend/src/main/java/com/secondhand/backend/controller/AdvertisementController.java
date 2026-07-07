package com.secondhand.backend.controller;

import com.secondhand.backend.model.Advertisement;
import com.secondhand.backend.service.AdvertisementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for handling advertisement-related HTTP requests.
 * <p>
 * Exposes endpoints for client applications (such as the JavaFX frontend)
 * to interact with the advertisement resources. All endpoints in this controller
 * are mapped under the "/api/ads" base path.
 * </p>
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/ads")
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    /**
     * Fetches all active advertisements for public display.
     *
     * @return A JSON array containing {@link Advertisement} objects with ACTIVE status.
     * Endpoint: GET /api/ads/active
     */
    @GetMapping("/active")
    public List<Advertisement> getActiveAds() {
        return advertisementService.getActiveAdvertisements();
    }

    /**
     * Creates a new advertisement in the system.
     *
     * @param advertisement The advertisement data sent as a JSON payload in the request body.
     * @return The created {@link Advertisement} object as JSON, including its generated ID.
     * Endpoint: POST /api/ads
     */
    @PostMapping
    public Advertisement createAdvertisement(@RequestBody Advertisement advertisement) {
        return advertisementService.saveAdvertisement(advertisement);
    }

    /**
     * Fetches all advertisements (including pending, sold, and rejected) for the admin panel.
     *
     * @return A JSON array containing all {@link Advertisement} objects.
     * Endpoint: GET /api/ads/all
     */
    @GetMapping("/all")
    public List<Advertisement> getAllAds() {
        return advertisementService.getAllAdvertisementsForAdmin();
    }

    /**
     * Endpoint to filter active advertisements by a specific city ID.
     *
     * @param cityId The ID of the city passed in the URL path.
     * @return A list of matching {@link Advertisement} objects.
     * Endpoint: GET /api/ads/city/{cityId}
     */
    @GetMapping("/city/{cityId}")
    public List<Advertisement> getAllAdsByCity(@PathVariable Long cityId) {
        return advertisementService.getActiveAdsByCity(cityId);
    }

    /**
     * Endpoint to filter active advertisements by a specific category ID.
     *
     * @param categoryId The ID of the category passed in the URL path.
     * @return A list of matching {@link Advertisement} objects.
     * Endpoint: GET /api/ads/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public List<Advertisement> getAdsByCategory(@PathVariable Long categoryId) {
        return advertisementService.getActiveAdsByCategory(categoryId);
    }

    /**
     * Endpoint to filter active advertisements within a specific price range.
     *
     * @param min The minimum acceptable price.
     * @param max The maximum acceptable price.
     * @return A list of matching {@link Advertisement} objects.
     * Endpoint: GET /api/ads/price?min=X&max=Y
     */
    @GetMapping("/price")
    public List<Advertisement> getAdsByPrice(@RequestParam Long min, @RequestParam Long max) {
        return advertisementService.getActiveAdsByPriceRange(min, max);
    }
}
