Project Overview

SpringBoot-CommodityWebScraper is a Spring Boot-based REST API designed for managing and retrieving market data on various commodities. It features comprehensive endpoints for fetching details about commodities, including names, categories, prices, news, forecasts, and descriptions. The project uses Selenium for web scraping and persists data using JPA repositories.

Features

RESTful API endpoints to retrieve commodity data.
Web scraping functionality using Selenium.
Data persistence using Spring Data JPA.
Exception handling with a global error handling mechanism.
Spring Security for basic API protection.

Technologies Used

Spring Boot
Spring Data JPA
Spring Security
Selenium WebDriver
MySQL

Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites
Java JDK 17
Maven
MySQL

Setting Up

Clone the repository:
git clone https://github.com/roshangit23/SpringBoot-CommodityWebScraper.git

Navigate to the project directory:
cd SpringBoot-CommodityWebScraper

Configure application.properties with your MySQL credentials and database URL.

Run the application:
mvn spring-boot:run

Some of the API Endpoints

Commodity Data

GET /api/commodities/commodityNames: Retrieve all commodity names.
GET /api/commodities/categoryNames: Fetch all commodity category names.
GET /api/commodities/names/{category}: Get commodity names by category.
GET /api/commodities/prices: List all commodity prices.
GET /api/commodities/news: Get latest commodity news.
GET /api/commodities/forecasts: Access commodity forecasts.
GET /api/commodities/descriptions: View commodity descriptions.

Database Direct Access

GET /api/commodities/db/commodityNames: Fetch commodity names from the database.
GET /api/commodities/db/categoryNames: Retrieve category names from the database.
GET /api/commodities/db/namesByCategory/{category}: Retrieve commodity names by category from the database.
GET /api/commodities/db/prices/date/{date}: Get commodity prices by date from the database.
GET /api/commodities/db/news/date/{date}: Access commodity news by date from the database.
GET /api/commodities/db/forecasts/date/{date}: Access commodity forecasts by date from the database.
GET /api/commodities/db/descriptions/date/{date}: Access commodity descriptions by date from the database.

Exception Handling

Global exception handling is managed using @ControllerAdvice to handle various exceptions like ResourceNotFoundException, DataIntegrityViolationException, MethodArgumentTypeMismatchException, etc., providing appropriate HTTP status codes and error messages.

Entities

Commodity
CommodityCategory
CommodityDescription
CommodityForecast
CommodityNews
CommodityPrice

Repository Interfaces

CommodityRepository
CommodityCategoryRepository
CommodityDescriptionRepository
CommodityForecastRepository
CommodityNewsRepository
CommodityPriceRepository

Services

CommodityDataService
CommodityDataScraperService