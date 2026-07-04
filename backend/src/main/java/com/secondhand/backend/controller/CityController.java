package com.secondhand.backend.controller;

import com.secondhand.backend.model.City;
import com.secondhand.backend.service.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public City createCity(@RequestBody City city) {
        return cityService.saveCity(city);
    }

    @GetMapping("/all")
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }
}
