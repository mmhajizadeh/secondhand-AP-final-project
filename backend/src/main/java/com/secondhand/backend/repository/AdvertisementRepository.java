package com.secondhand.backend.repository;

import com.secondhand.backend.model.Advertisement;
import com.secondhand.backend.model.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByStatus(AdvertisementStatus status);
}
