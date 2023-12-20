package com.marketdata.commoditiesapi.service;

import com.marketdata.commoditiesapi.model.*;
import com.marketdata.commoditiesapi.repository.*;
import com.marketdata.commoditiesapi.util.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommodityDataScraperService {
    @Autowired
    private CommodityDescriptionRepository descriptionRepository;
    @Autowired
    private CommodityForecastRepository forecastRepository;
    @Autowired
    private CommodityNewsRepository newsRepository;
    @Autowired
    private CommodityPriceRepository priceRepository;
    @Autowired
    private CommodityCategoryRepository commodityCategoryRepository;
    @Autowired
    private CommodityRepository commodityRepository;

    public List<String> getCategoryNames() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");

        List<WebElement> categoryElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/thead/tr/th[1]"));
        List<String> categoryNames = new ArrayList<>();
        for (int i = 0; i < categoryElements.size(); i++) {
            categoryNames.add(categoryElements.get(i).getText());
        }
        DriverFactory.closeDriver();
        return categoryNames;
    }

    public List<String> getCommodityNamesByCategory(String category) {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");

        List<WebElement> categoryElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/thead/tr/th[1]"));
        int categoryIndex = -1;

        for (int i = 0; i < categoryElements.size(); i++) {
            if (categoryElements.get(i).getText().equalsIgnoreCase(category)) {
                categoryIndex = i + 1;  // +1 because XPath indexing starts at 1
                break;
            }
        }

        List<String> commodityNames = new ArrayList<>();
        if (categoryIndex != -1) {
            String xpathForNames = String.format("(//table[@class='table table-hover table-striped table-heatmap'])[%d]/tbody/tr/td/a/b", categoryIndex);
            List<WebElement> nameElements = driver.findElements(By.xpath(xpathForNames));

            for (WebElement nameElement : nameElements) {
                commodityNames.add(nameElement.getText());
            }
        }
        for (String commodityName : commodityNames) {
            if (!commodityCategoryRepository.existsByCommodityNameAndCategoryName(commodityName, category)) {
                CommodityCategory commodityCategory = new CommodityCategory(commodityName, category);
                commodityCategoryRepository.save(commodityCategory);
            }
        }

        DriverFactory.closeDriver();
        return commodityNames;
    }

    public List<String> getCommodityNames() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");

        List<String> commodityNames = new ArrayList<>();

        List<WebElement> nameElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td/a/b"));

        for (WebElement nameElement : nameElements) {
            commodityNames.add(nameElement.getText());
        }

        for (String commodityName : commodityNames) {
            if (!commodityRepository.existsByName(commodityName)) {
                Commodity commodity = new Commodity(commodityName);
                commodityRepository.save(commodity);
            }
        }

        DriverFactory.closeDriver();
        return commodityNames;
    }

    public List<CommodityPrice> scrapePrices() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");
        LocalDate today = LocalDate.now();
        List<CommodityPrice> todayPrices = new ArrayList<>();

        List<WebElement> nameElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td/a/b"));
        List<WebElement> priceElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[2]"));
        List<WebElement> dateElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[8]"));
        List<WebElement> changeInValueElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[3]"));
        List<WebElement> percentageChangeElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[4]"));
        List<WebElement> weeklyPercentageChangeElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[5]"));
        List<WebElement> monthlyPercentageChangeElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[6]"));
        List<WebElement> yearlyPercentageChangeElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[7]"));

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MMM/dd")
                .parseDefaulting(ChronoField.YEAR, Year.now().getValue())
                .toFormatter();

        for (int i = 0; i < nameElements.size(); i++) {
            String name = nameElements.get(i).getText();
            double priceValue = Double.parseDouble(priceElements.get(i).getText().replaceAll("[^\\d.]", ""));
            double changeInValue = Double.parseDouble(changeInValueElements.get(i).getText().replaceAll("[^\\d.-]", ""));
            double percentageChange = Double.parseDouble(percentageChangeElements.get(i).getText().replaceAll("[^\\d.-]", ""));
            double weeklyPercentageChange = Double.parseDouble(weeklyPercentageChangeElements.get(i).getText().replaceAll("[^\\d.-]", ""));
            double monthlyPercentageChange = Double.parseDouble(monthlyPercentageChangeElements.get(i).getText().replaceAll("[^\\d.-]", ""));
            double yearlyPercentageChange = Double.parseDouble(yearlyPercentageChangeElements.get(i).getText().replaceAll("[^\\d.-]", ""));

            String dateString = dateElements.get(i).getText();
            LocalDate date;
            if (dateString.contains(":")) {
                date = today;
            } else {
                date = LocalDate.parse(dateString, formatter);
            }

            Optional<CommodityPrice> existingPrice = priceRepository.findByNameAndDate(name, today);
            CommodityPrice price = existingPrice.orElse(new CommodityPrice(name, priceValue, changeInValue, percentageChange, weeklyPercentageChange, monthlyPercentageChange, yearlyPercentageChange, date));
            price.setPrice(priceValue);
            price.setChangeInValue(changeInValue);
            price.setPercentageChange(percentageChange);
            price.setWeeklyPercentageChange(weeklyPercentageChange);
            price.setMonthlyPercentageChange(monthlyPercentageChange);
            price.setYearlyPercentageChange(yearlyPercentageChange);
            priceRepository.save(price);
            todayPrices.add(price);
        }

        DriverFactory.closeDriver();
        return todayPrices;
    }

    public Optional<CommodityPrice> scrapePricesByName(String commodityName) {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");
        LocalDate today = LocalDate.now();
        List<WebElement> nameElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td/a/b"));
        List<WebElement> priceElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[2]"));
        List<WebElement> dateElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[8]"));
        List<WebElement> changeInValueElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[3]"));
        List<WebElement> percentageChangeElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[4]"));
        List<WebElement> weeklyPercentageChangeElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[5]"));
        List<WebElement> monthlyPercentageChangeElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[6]"));
        List<WebElement> yearlyPercentageChangeElements = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td[7]"));

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MMM/dd")
                .parseDefaulting(ChronoField.YEAR, Year.now().getValue())
                .toFormatter();
        try {
            for (int i = 0; i < nameElements.size(); i++) {
                String name = nameElements.get(i).getText();
                if (name.equalsIgnoreCase(commodityName)) {
                    double priceValue = Double.parseDouble(priceElements.get(i).getText().replaceAll("[^\\d.]", ""));
                    double changeInValue = Double.parseDouble(changeInValueElements.get(i).getText().replaceAll("[^\\d.-]", ""));
                    double percentageChange = Double.parseDouble(percentageChangeElements.get(i).getText().replaceAll("[^\\d.-]", ""));
                    double weeklyPercentageChange = Double.parseDouble(weeklyPercentageChangeElements.get(i).getText().replaceAll("[^\\d.-]", ""));
                    double monthlyPercentageChange = Double.parseDouble(monthlyPercentageChangeElements.get(i).getText().replaceAll("[^\\d.-]", ""));
                    double yearlyPercentageChange = Double.parseDouble(yearlyPercentageChangeElements.get(i).getText().replaceAll("[^\\d.-]", ""));

                    String dateString = dateElements.get(i).getText();
                    LocalDate date;
                    if (dateString.contains(":")) {
                        date = today;
                    } else {
                        date = LocalDate.parse(dateString, formatter);
                    }

                    Optional<CommodityPrice> existingPrice = priceRepository.findByNameAndDate(name, today);
                    CommodityPrice price = existingPrice.orElse(new CommodityPrice(name, priceValue, changeInValue, percentageChange, weeklyPercentageChange, monthlyPercentageChange, yearlyPercentageChange, today));

                    price.setPrice(priceValue);
                    price.setChangeInValue(changeInValue);
                    price.setPercentageChange(percentageChange);
                    price.setWeeklyPercentageChange(weeklyPercentageChange);
                    price.setMonthlyPercentageChange(monthlyPercentageChange);
                    price.setYearlyPercentageChange(yearlyPercentageChange);

                    priceRepository.save(price);
                    return Optional.of(price);
                }
            }
        } finally {
            DriverFactory.closeDriver();
        }
        return Optional.empty();
    }

    public List<CommodityNews> scrapeNews() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");
        LocalDate today = LocalDate.now();
        List<WebElement> commodityLinks = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td/a"));
        List<CommodityNews> todaysNewsList = new ArrayList<>();

        for (WebElement link : commodityLinks) {
            String commodityName = link.getText();
            link.click(); // Click on the commodity link

            WebElement newsElement = driver.findElement(By.xpath("//h2[@id='description']"));
            String newsContent = newsElement.getText();

            Optional<CommodityNews> existingNews = newsRepository.findByNameAndPublishDate(commodityName, today);
            CommodityNews news = existingNews.orElse(new CommodityNews(commodityName, newsContent));
            news.setContent(newsContent); // Update content in case of existing news
            newsRepository.save(news);
            todaysNewsList.add(news);
            driver.navigate().back(); // Navigate back to the main page
        }

        DriverFactory.closeDriver();
        return todaysNewsList;
    }


    public Optional<CommodityNews> scrapeNewsByName(String commodityName) {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");
        LocalDate today = LocalDate.now();
        List<WebElement> commodityLinks = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td/a"));

        for (WebElement link : commodityLinks) {
            if (link.getText().equalsIgnoreCase(commodityName)) {
                link.click(); // Click on the specific commodity link

                try {
                    WebElement newsElement = driver.findElement(By.xpath("//h2[@id='description']"));
                    String newsContent = newsElement.getText();

                    Optional<CommodityNews> existingNews = newsRepository.findByNameAndPublishDate(commodityName, today);
                    CommodityNews news = existingNews.orElse(new CommodityNews(commodityName, newsContent));
                    news.setContent(newsContent); // Update content in case of existing news
                    newsRepository.save(news);

                    driver.navigate().back(); // Navigate back to the main page
                    return Optional.of(news);
                } finally {
                    DriverFactory.closeDriver();
                }
            }
        }
        return Optional.empty();
    }

    public List<CommodityForecast> scrapeForecasts() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");

        List<WebElement> commodityLinks = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td/a"));
        LocalDate today = LocalDate.now();
        List<CommodityForecast> todaysForecasts = new ArrayList<>();

        for (WebElement link : commodityLinks) {
            String commodityName = link.getText();
            link.click(); // Click on the commodity link

            WebElement forecastLink = driver.findElement(By.xpath("//a[@href='#forecast']"));
            forecastLink.click(); // Click on the forecast link

            WebElement forecastTextElement = driver.findElement(By.xpath("//h3"));
            String forecastText = forecastTextElement.getText();

            Optional<CommodityForecast> existingForecast = forecastRepository.findByNameAndDate(commodityName, today);
            CommodityForecast forecast = existingForecast.orElse(new CommodityForecast(commodityName, forecastText));
            forecast.setForecast(forecastText); // Update text in case of existing forecast
            forecastRepository.save(forecast);
            todaysForecasts.add(forecast);

            driver.navigate().back(); // Navigate back to the main page

        }

        DriverFactory.closeDriver();
        return todaysForecasts;
    }

    public Optional<CommodityForecast> scrapeForecastByName(String commodityName) {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");
        LocalDate today = LocalDate.now();
        List<WebElement> commodityLinks = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td/a"));

        for (WebElement link : commodityLinks) {
            if (link.getText().equalsIgnoreCase(commodityName)) {
                link.click(); // Click on the specific commodity link

                try {
                    WebElement forecastLink = driver.findElement(By.xpath("//a[@href='#forecast']"));
                    forecastLink.click(); // Click on the forecast link

                    WebElement forecastTextElement = driver.findElement(By.xpath("//h3"));
                    String forecastText = forecastTextElement.getText();

                    Optional<CommodityForecast> existingForecast = forecastRepository.findByNameAndDate(commodityName, today);
                    CommodityForecast forecast = existingForecast.orElse(new CommodityForecast(commodityName, forecastText));
                    forecast.setForecast(forecastText); // Update text in case of existing forecast
                    forecastRepository.save(forecast);
                    driver.navigate().back(); // Navigate back to the main page
                    return Optional.of(forecast);
                } finally {
                    DriverFactory.closeDriver();
                }
            }
        }
        return Optional.empty();
    }

    public List<CommodityDescription> scrapeDescriptions() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");
        LocalDate today = LocalDate.now();
        List<CommodityDescription> todayDescriptions = new ArrayList<>();

        List<WebElement> commodityLinks = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td/a"));

        for (WebElement link : commodityLinks) {
            String commodityName = link.getText();
            link.click(); // Click on the commodity link

            WebElement descriptionElement = driver.findElement(By.xpath("//div[@class='card']/div[@class='card-body']"));
            String descriptionText = descriptionElement.getText();

            Optional<CommodityDescription> existingDescription = descriptionRepository.findByNameAndDate(commodityName, today);
            CommodityDescription description = existingDescription.orElse(new CommodityDescription(commodityName, descriptionText));
            description.setDescription(descriptionText); // Update text in case of existing description
            descriptionRepository.save(description);
            todayDescriptions.add(description);

            driver.navigate().back(); // Navigate back to the main page
        }

        DriverFactory.closeDriver();
        return todayDescriptions;
    }

    public Optional<CommodityDescription> scrapeDescriptionByName(String commodityName) {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("https://tradingeconomics.com/commodities");
        LocalDate today = LocalDate.now();

        List<WebElement> commodityLinks = driver.findElements(By.xpath("//table[@class='table table-hover table-striped table-heatmap']/tbody/tr/td/a"));

        for (WebElement link : commodityLinks) {
            if (link.getText().equalsIgnoreCase(commodityName)) {
                link.click(); // Click on the specific commodity link

                try {
                    WebElement descriptionElement = driver.findElement(By.xpath("//div[@class='card']/div[@class='card-body']"));
                    String descriptionText = descriptionElement.getText();

                    // Check for existing description
                    Optional<CommodityDescription> existingDescription = descriptionRepository.findByNameAndDate(commodityName, today);
                    CommodityDescription description = existingDescription.orElse(new CommodityDescription(commodityName, descriptionText));
                    description.setDescription(descriptionText); // Update text in case of existing description
                    descriptionRepository.save(description);

                    driver.navigate().back(); // Navigate back to the main page
                    return Optional.of(description);
                } finally {
                    DriverFactory.closeDriver();
                }
            }
        }
        return Optional.empty();
    }

}
