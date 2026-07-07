package com.secondhand.backend.service;

import com.secondhand.backend.model.Advertisement;
import com.secondhand.backend.model.AdvertisementStatus;
import com.secondhand.backend.repository.AdvertisementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing {@link Advertisement} entities.
 * <p>
 * This class handles all the business logic related to advertisements,
 * acting as a bridge between the REST controllers and the data access layer.
 * </p>
 *
 * @author mmhajizadeh
 * @since 1.0
 */
@Service
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;

    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    /**
     * Retrieves a list of all active advertisements.
     * This method is primarily used for the main feed where users browse available items.
     *
     * @return A list of {@link Advertisement} objects that have an ACTIVE status.
     * @see AdvertisementStatus#ACTIVE
     */
    public List<Advertisement> getActiveAdvertisements() {
        return advertisementRepository.findByStatus(AdvertisementStatus.ACTIVE);
    }

    /**
     * Saves a newly created advertisement to the database.
     * By default, new advertisements are saved with a PENDING status and require admin approval.
     *
     * @param newAd The {@link Advertisement} object to be saved. Must contain valid city and category references.
     * @return The saved {@link Advertisement} entity containing the auto-generated database ID.
     */
    public Advertisement saveAdvertisement(Advertisement newAd) {
        return advertisementRepository.save(newAd);
    }

    /**
     * Retrieves all advertisements regardless of their current status.
     * This method is intended for administrative purposes (e.g., admin dashboard).
     *
     * @return A complete list of all {@link Advertisement} objects in the database.
     */
    public List<Advertisement> getAllAdvertisementsForAdmin() {
        return advertisementRepository.findAll();
    }

    /**
     * Retrieves active advertisements filtered by city.
     *
     * @param cityId The ID of the city.
     * @return A list of active {@link Advertisement} objects in the specified city.
     */
    public List<Advertisement> getActiveAdsByCity(Long cityId) {
        return advertisementRepository.findByCityIdAndStatus(cityId, AdvertisementStatus.ACTIVE);
    }

    /**
     * Retrieves active advertisements filtered by category.
     *
     * @param categoryId The ID of the category.
     * @return A list of active {@link Advertisement} objects in the specified category.
     */
    public List<Advertisement> getActiveAdsByCategory(Long categoryId) {
        return advertisementRepository.findByCategoryIdAndStatus(categoryId, AdvertisementStatus.ACTIVE);
    }

    /**
     * Retrieves active advertisements within a specific price range.
     *
     * @param minPrice The minimum price.
     * @param maxPrice The maximum price.
     * @return A list of active {@link Advertisement} objects within the price range.
     */
    public List<Advertisement> getActiveAdsByPriceRange(Long minPrice, Long maxPrice) {
        return advertisementRepository.findByPriceBetweenAndStatus(minPrice, maxPrice, AdvertisementStatus.ACTIVE);
    }
}
