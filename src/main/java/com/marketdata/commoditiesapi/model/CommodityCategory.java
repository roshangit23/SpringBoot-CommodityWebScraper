package com.marketdata.commoditiesapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "commodity_categories", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"commodityName", "categoryName"})
})
public class CommodityCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commodityName;
    private String categoryName;

    // Constructors, Getters, and Setters

    public CommodityCategory(String commodityName, String categoryName) {
        this.commodityName = commodityName;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}