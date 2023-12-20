package com.marketdata.commoditiesapi.service;

import com.marketdata.commoditiesapi.model.CommodityDescription;
import com.marketdata.commoditiesapi.model.CommodityForecast;
import com.marketdata.commoditiesapi.model.CommodityNews;
import com.marketdata.commoditiesapi.model.CommodityPrice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommodityDataService {
    List<String> getCommodityNames();
    List<String> getCategoryNames();
    List<String> getCommodityNamesByCategory(String category);
    List<CommodityPrice> getCommodityPricesByDate(LocalDate date);
    Optional<CommodityPrice> getCommodityPricesByNameAndDate(String name, LocalDate date);
    List<CommodityNews> getCommodityNewsByDate(LocalDate date);
    Optional<CommodityNews> getCommodityNewsByNameAndDate(String name, LocalDate date);
    List<CommodityForecast> getCommodityForecastsByDate(LocalDate date);
    Optional<CommodityForecast> getCommodityForecastsByNameAndDate(String name, LocalDate date);
    List<CommodityDescription> getCommodityDescriptionsByDate(LocalDate date);
    Optional<CommodityDescription> getCommodityDescriptionsByNameAndDate(String name, LocalDate date);
}
