package com.marketdata.commoditiesapi.repository;

import com.marketdata.commoditiesapi.model.CommodityForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommodityForecastRepository extends JpaRepository<CommodityForecast, Long> {
    Optional<CommodityForecast> findByNameAndDate(String commodityName, LocalDate today);

    List<CommodityForecast> findByDate(LocalDate date);
    // Custom database queries if needed
}
