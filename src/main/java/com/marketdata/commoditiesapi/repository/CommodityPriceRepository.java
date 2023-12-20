package com.marketdata.commoditiesapi.repository;

import com.marketdata.commoditiesapi.model.CommodityPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommodityPriceRepository extends JpaRepository<CommodityPrice, Long> {
    Optional<CommodityPrice> findByNameAndDate(String name, LocalDate today);

    List<CommodityPrice> findByDate(LocalDate date);
}


