package com.marketdata.commoditiesapi.repository;

import com.marketdata.commoditiesapi.model.CommodityDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommodityDescriptionRepository extends JpaRepository<CommodityDescription, Long> {
    Optional<CommodityDescription> findByNameAndDate(String name, LocalDate date);

    List<CommodityDescription> findByDate(LocalDate date);
}
