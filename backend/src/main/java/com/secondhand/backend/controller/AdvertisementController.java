package com.secondhand.backend.controller;

import com.secondhand.backend.model.Advertisement;
import com.secondhand.backend.service.AdvertisementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping("/active")
    public List<Advertisement> getActiveAds() {
        return advertisementService.getActiveAdvertisements();
    }

    @PostMapping
    public Advertisement createAdvertisement(@RequestBody Advertisement advertisement) {
        return advertisementService.saveAdvertisement(advertisement);
    }

    @GetMapping("/all")
    public List<Advertisement> getAllAds() {
        return advertisementService.getAllAdvertisementsForAdmin();
    }
}
