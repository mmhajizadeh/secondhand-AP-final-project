package com.secondhand.backend.repository;

import com.secondhand.backend.entity.Advertisement;
import com.secondhand.backend.entity.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByStatus(AdvertisementStatus status);
    List<Advertisement> findByCityIdAndStatus(Long cityId, AdvertisementStatus status);
    List<Advertisement> findByCategoryIdAndStatus(Long categoryId, AdvertisementStatus status);
    List<Advertisement> findByPriceBetweenAndStatus(Long minPrice, Long maxPrice, AdvertisementStatus status);
}
