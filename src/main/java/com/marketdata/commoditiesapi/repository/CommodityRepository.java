package com.marketdata.commoditiesapi.repository;

import com.marketdata.commoditiesapi.model.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommodityRepository extends JpaRepository<Commodity, Long> {
    boolean existsByName(String commodityName);
}
