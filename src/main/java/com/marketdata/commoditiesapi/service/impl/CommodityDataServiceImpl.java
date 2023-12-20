package com.marketdata.commoditiesapi.service.impl;

import com.marketdata.commoditiesapi.model.*;
import com.marketdata.commoditiesapi.repository.*;
import com.marketdata.commoditiesapi.service.CommodityDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommodityDataServiceImpl implements CommodityDataService {

    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private CommodityCategoryRepository commodityCategoryRepository;
    @Autowired
    private CommodityPriceRepository priceRepository;
    @Autowired
    private CommodityNewsRepository newsRepository;
    @Autowired
    private CommodityForecastRepository forecastRepository;
    @Autowired
    private CommodityDescriptionRepository descriptionRepository;

    @Override
    public List<String> getCommodityNames() {
        return commodityRepository.findAll().stream()
                .map(Commodity::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCategoryNames() {
        return commodityCategoryRepository.findAll().stream()
                .map(CommodityCategory::getCategoryName)
                .distinct() // Ensure unique category names
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCommodityNamesByCategory(String category) {
        return commodityCategoryRepository.findByCategoryName(category).stream()
                .map(CommodityCategory::getCommodityName)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommodityPrice> getCommodityPricesByDate(LocalDate date) {
        return priceRepository.findByDate(date);
    }

    @Override
    public Optional<CommodityPrice> getCommodityPricesByNameAndDate(String name, LocalDate date) {
        return priceRepository.findByNameAndDate(name, date);
    }

    @Override
    public List<CommodityNews> getCommodityNewsByDate(LocalDate date) {
        return newsRepository.findByPublishDate(date);
    }

    @Override
    public Optional<CommodityNews> getCommodityNewsByNameAndDate(String name, LocalDate date) {
        return newsRepository.findByNameAndPublishDate(name, date);
    }

    @Override
    public List<CommodityForecast> getCommodityForecastsByDate(LocalDate date) {
        return forecastRepository.findByDate(date);
    }

    @Override
    public Optional<CommodityForecast> getCommodityForecastsByNameAndDate(String name, LocalDate date) {
        return forecastRepository.findByNameAndDate(name, date);
    }

    @Override
    public List<CommodityDescription> getCommodityDescriptionsByDate(LocalDate date) {
        return descriptionRepository.findByDate(date);
    }

    @Override
    public Optional<CommodityDescription> getCommodityDescriptionsByNameAndDate(String name, LocalDate date) {
        return descriptionRepository.findByNameAndDate(name, date);
    }
}

