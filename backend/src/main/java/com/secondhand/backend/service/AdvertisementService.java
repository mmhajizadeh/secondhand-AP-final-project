package com.secondhand.backend.service;

import com.secondhand.backend.model.Advertisement;
import com.secondhand.backend.model.AdvertisementStatus;
import com.secondhand.backend.repository.AdvertisementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;

    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    public List<Advertisement> getActiveAdvertisements() {
        return advertisementRepository.findByStatus(AdvertisementStatus.ACTIVE);
    }

    public Advertisement saveAdvertisement(Advertisement newAd) {
        return advertisementRepository.save(newAd);
    }

    public List<Advertisement> getAllAdvertisementsForAdmin() {
        return advertisementRepository.findAll();
    }
}
