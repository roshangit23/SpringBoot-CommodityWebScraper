package com.marketdata.commoditiesapi.repository;

import com.marketdata.commoditiesapi.model.CommodityNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommodityNewsRepository extends JpaRepository<CommodityNews, Long> {

    List<CommodityNews> findByPublishDate(LocalDate date);

    Optional<CommodityNews> findByNameAndPublishDate(String name, LocalDate date);
    // Custom database queries if needed
}
