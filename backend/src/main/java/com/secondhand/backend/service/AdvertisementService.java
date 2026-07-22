package com.secondhand.backend.service;

import com.secondhand.backend.entity.Advertisement;
import com.secondhand.backend.entity.AdvertisementStatus;
import com.secondhand.backend.repository.AdvertisementRepository;
import org.springframework.stereotype.Service;

import java.lang.RuntimeException;
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

    /**
     * Updates the details of an existing advertisement if the requesting user is the owner.
     *
     * @param id The ID of the advertisement to update.
     * @param updatedAd The new advertisement data.
     * @param requestingUsername The requesting user to update advertisement.
     * @return The updated {@link Advertisement} object.
     * @throws RuntimeException if the advertisement with the given ID is not found or access denied.
     */
    public Advertisement updateAdvertisement(Long id, Advertisement updatedAd, String requestingUsername) {
        // find Advertisement in database
        Advertisement existingAd = advertisementRepository.findById(id).orElseThrow(() -> new RuntimeException("Advertisement not found with id: " + id));

        if(!existingAd.getOwnerUsername().equals(requestingUsername)) {
            throw new RuntimeException("Access Denied: You can only edit your own advertisements.");
        }

        existingAd.setTitle(updatedAd.getTitle());
        existingAd.setDescription(updatedAd.getDescription());
        existingAd.setPrice(updatedAd.getPrice());
        existingAd.setCity(updatedAd.getCity());
        existingAd.setCategory(updatedAd.getCategory());

        return advertisementRepository.save(existingAd);
    }

    /**
     * Updates the status of an advertisement.
     * This is used by admins to approve/reject ads, or by owners to mark them as sold.
     *
     * @param id The ID of the advertisement.
     * @param newStatus The new {@link AdvertisementStatus} to apply.
     * @return The updated {@link Advertisement} object.
     * @throws RuntimeException if the advertisement is not found.
     */
    public Advertisement updateAdvertisementStatus(Long id, AdvertisementStatus newStatus) {
        Advertisement existingAd = advertisementRepository.findById(id).orElseThrow(() -> new RuntimeException("Advertisement not found with id: " + id));

        existingAd.setStatus(newStatus);
        return advertisementRepository.save(existingAd);
    }

    /**
     * Deletes an advertisement if the requesting user is the owner.
     *
     * @param id The ID of the advertisement to delete.
     * @param requestingUsername The requesting user to delete advertisement.
     * @throws RuntimeException if the advertisement is not found or access denied.
     */
    public void deleteAdvertisement(Long id, String requestingUsername) {
        Advertisement existingAd = advertisementRepository.findById(id).orElseThrow(() -> new RuntimeException("Advertisement not found with id: " + id));

        if (!existingAd.getOwnerUsername().equals(requestingUsername)) {
            throw new RuntimeException("Access Denied: You can only delete your own advertisements.");
        }

        advertisementRepository.delete(existingAd);
    }

    /**
     * Retrieves all advertisements owned by a specific user.
     * This ensures users can only query their own data for their dashboard.
     *
     * @param username The exact username of the advertisement owner.
     * @return A list of {@link Advertisement} objects belonging to the user.
     */
    public List<Advertisement> getAdsByOwnerUsername(String username) {
        return advertisementRepository.findByOwnerUsername(username);
    }
}
