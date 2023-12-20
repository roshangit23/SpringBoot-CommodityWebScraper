package com.marketdata.commoditiesapi.controller;

import com.marketdata.commoditiesapi.model.CommodityDescription;
import com.marketdata.commoditiesapi.model.CommodityForecast;
import com.marketdata.commoditiesapi.model.CommodityNews;
import com.marketdata.commoditiesapi.model.CommodityPrice;
import com.marketdata.commoditiesapi.service.CommodityDataScraperService;
import com.marketdata.commoditiesapi.service.CommodityDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/commodities")
public class CommodityController {
    @Autowired
    private CommodityDataScraperService scraperService;
    @Autowired
    CommodityDataService commodityDataService;
    @GetMapping("/commodityNames")
    public ResponseEntity<List<String>> getCommodityNames() {
        List<String> commodityNames = scraperService.getCommodityNames();
        return ResponseEntity.ok(commodityNames);
    }
    @GetMapping("/categoryNames")
    public ResponseEntity<List<String>> getCategoryNames() {
        List<String> categoryNames = scraperService.getCategoryNames();
        return ResponseEntity.ok(categoryNames);
    }
    @GetMapping("/names/{category}")
    public ResponseEntity<List<String>> getCommodityNamesByCategory(@PathVariable String category) {
        List<String> names = scraperService.getCommodityNamesByCategory(category);
        return ResponseEntity.ok(names);
    }

    @GetMapping("/prices")
    public ResponseEntity<List<CommodityPrice>> getCommodityPrices() {
        List<CommodityPrice> prices = scraperService.scrapePrices();
        return ResponseEntity.ok(prices);
    }
    @GetMapping("/prices/{name}")
    public ResponseEntity<Optional<CommodityPrice>> getCommodityPricesByName(@PathVariable String name) {
        Optional<CommodityPrice> prices = scraperService.scrapePricesByName(name);
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/news")
    public ResponseEntity<List<CommodityNews>> getCommodityNews() {
        List<CommodityNews> news = scraperService.scrapeNews();
        return ResponseEntity.ok(news);
    }
    @GetMapping("/news/{name}")
    public ResponseEntity<Optional<CommodityNews>> getCommodityNewsByName(@PathVariable String name) {
        Optional<CommodityNews> news = scraperService.scrapeNewsByName(name);
        return ResponseEntity.ok(news);
    }
    @GetMapping("/forecasts")
    public ResponseEntity<List<CommodityForecast>> getCommodityForecasts() {
        List<CommodityForecast> forecasts = scraperService.scrapeForecasts();
        return ResponseEntity.ok(forecasts);
    }
    @GetMapping("/forecasts/{name}")
    public ResponseEntity<Optional<CommodityForecast>> getCommodityForecastsByName(@PathVariable String name) {
        Optional<CommodityForecast> forecasts = scraperService.scrapeForecastByName(name);
        return ResponseEntity.ok(forecasts);
    }
    @GetMapping("/descriptions")
    public ResponseEntity<List<CommodityDescription>> getCommodityDescriptions() {
        List<CommodityDescription> descriptions = scraperService.scrapeDescriptions();
        return ResponseEntity.ok(descriptions);
    }

    @GetMapping("/descriptions/{name}")
    public ResponseEntity<Optional<CommodityDescription>> getCommodityDescriptionsByName(@PathVariable String name) {
        Optional<CommodityDescription> descriptions = scraperService.scrapeDescriptionByName(name);
        return ResponseEntity.ok(descriptions);
    }

    //APIs for fetching directly from database
    @GetMapping("/db/commodityNames")
    public ResponseEntity<List<String>> getDbCommodityNames() {
        List<String> commodityNames = commodityDataService.getCommodityNames();
        return ResponseEntity.ok(commodityNames);
    }

    @GetMapping("/db/categoryNames")
    public ResponseEntity<List<String>> getDbCategoryNames() {
        List<String> categoryNames = commodityDataService.getCategoryNames();
        return ResponseEntity.ok(categoryNames);
    }

    @GetMapping("/db/namesByCategory/{category}")
    public ResponseEntity<List<String>> getDbCommodityNamesByCategory(@PathVariable String category) {
        List<String> names = commodityDataService.getCommodityNamesByCategory(category);
        return ResponseEntity.ok(names);
    }

    // CommodityPrice Endpoints
    @GetMapping("/db/prices/date/{date}")
    public ResponseEntity<List<CommodityPrice>> getDbCommodityPricesByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(commodityDataService.getCommodityPricesByDate(localDate));
    }

    @GetMapping("/db/prices/{name}/{date}")
    public ResponseEntity<Optional<CommodityPrice>> getDbCommodityPricesByNameAndDate(@PathVariable String name, @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(commodityDataService.getCommodityPricesByNameAndDate(name, localDate));
    }

    // CommodityNews Endpoints
    @GetMapping("/db/news/date/{date}")
    public ResponseEntity<List<CommodityNews>> getDbCommodityNewsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(commodityDataService.getCommodityNewsByDate(localDate));
    }

    @GetMapping("/db/news/{name}/{date}")
    public ResponseEntity<Optional<CommodityNews>> getDbCommodityNewsByNameAndDate(@PathVariable String name, @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(commodityDataService.getCommodityNewsByNameAndDate(name, localDate));
    }

    // CommodityForecast Endpoints
    @GetMapping("/db/forecasts/date/{date}")
    public ResponseEntity<List<CommodityForecast>> getDbCommodityForecastsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(commodityDataService.getCommodityForecastsByDate(localDate));
    }

    @GetMapping("/db/forecasts/{name}/{date}")
    public ResponseEntity<Optional<CommodityForecast>> getDbCommodityForecastsByNameAndDate(@PathVariable String name, @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(commodityDataService.getCommodityForecastsByNameAndDate(name, localDate));
    }

    // CommodityDescription Endpoints
    @GetMapping("/db/descriptions/date/{date}")
    public ResponseEntity<List<CommodityDescription>> getDbCommodityDescriptionsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(commodityDataService.getCommodityDescriptionsByDate(localDate));
    }

    @GetMapping("/db/descriptions/{name}/{date}")
    public ResponseEntity<Optional<CommodityDescription>> getDbCommodityDescriptionsByNameAndDate(@PathVariable String name, @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(commodityDataService.getCommodityDescriptionsByNameAndDate(name, localDate));
    }
}
