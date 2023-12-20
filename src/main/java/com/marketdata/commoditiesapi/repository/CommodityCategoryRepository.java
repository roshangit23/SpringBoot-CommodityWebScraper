package com.marketdata.commoditiesapi.repository;

import com.marketdata.commoditiesapi.model.CommodityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommodityCategoryRepository extends JpaRepository<CommodityCategory, Long> {

    boolean existsByCommodityNameAndCategoryName(String commodityName, String category);

    List<CommodityCategory> findByCategoryName(String category);
}
