package com.secondhand.backend.controller;

import com.secondhand.backend.entity.City;
import com.secondhand.backend.service.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing city-related HTTP requests.
 * <p>
 * Exposes endpoints to create and retrieve cities. All endpoints
 * in this controller are mapped under the "/api/cities" base path.
 * </p>
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Creates a new city in the system.
     *
     * @param city The city data sent as a JSON payload in the request body.
     * @return The created {@link City} object as JSON.
     * Endpoint: POST /api/cities
     */
    @PostMapping
    public City createCity(@RequestBody City city) {
        return cityService.saveCity(city);
    }

    /**
     * Fetches all available cities.
     *
     * @return A JSON array containing all {@link City} objects.
     * Endpoint: GET /api/cities/all
     */
    @GetMapping("/all")
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }
}
