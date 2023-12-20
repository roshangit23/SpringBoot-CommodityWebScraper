package com.marketdata.commoditiesapi.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "commodity_prices")
public class CommodityPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private double changeInValue;
    private double percentageChange;
    private double weeklyPercentageChange;
    private double monthlyPercentageChange;
    private double yearlyPercentageChange;
    private LocalDate date;

    // Constructors
    public CommodityPrice() {}

    public CommodityPrice(String name, double price, double changeInValue, double percentageChange, double weeklyPercentageChange, double monthlyPercentageChange, double yearlyPercentageChange, LocalDate date) {
        this.name = name;
        this.price = price;
        this.changeInValue = changeInValue;
        this.percentageChange = percentageChange;
        this.weeklyPercentageChange = weeklyPercentageChange;
        this.monthlyPercentageChange = monthlyPercentageChange;
        this.yearlyPercentageChange = yearlyPercentageChange;
        this.date = date;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getChangeInValue() {
        return changeInValue;
    }

    public void setChangeInValue(double changeInValue) {
        this.changeInValue = changeInValue;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public double getWeeklyPercentageChange() {
        return weeklyPercentageChange;
    }

    public void setWeeklyPercentageChange(double weeklyPercentageChange) {
        this.weeklyPercentageChange = weeklyPercentageChange;
    }

    public double getMonthlyPercentageChange() {
        return monthlyPercentageChange;
    }

    public void setMonthlyPercentageChange(double monthlyPercentageChange) {
        this.monthlyPercentageChange = monthlyPercentageChange;
    }

    public double getYearlyPercentageChange() {
        return yearlyPercentageChange;
    }

    public void setYearlyPercentageChange(double yearlyPercentageChange) {
        this.yearlyPercentageChange = yearlyPercentageChange;
    }
}
