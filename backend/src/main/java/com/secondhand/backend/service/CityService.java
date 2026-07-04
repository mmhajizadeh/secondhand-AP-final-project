package com.secondhand.backend.service;

import com.secondhand.backend.model.City;
import com.secondhand.backend.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing {@link City} entities.
 * <p>
 * Handles business logic operations related to cities, such as adding new cities
 * and retrieving the list of available cities for filtering advertisements.
 * </p>
 *
 * @since 1.0
 */
@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    /**
     * Saves a newly created city to the database.
     *
     * @param city The {@link City} object to be saved.
     * @return The saved {@link City} entity containing the auto-generated database ID.
     */
    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    /**
     * Retrieves a complete list of all supported cities in the system.
     * This is typically used to populate dropdown menus in the frontend.
     *
     * @return A list of all {@link City} objects.
     */
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}
