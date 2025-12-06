package com.pricecompare.price_compare.dto;

import java.math.BigDecimal;

public class ScrapedOfferDto {
    private String siteName;
    private String title;
    private BigDecimal price;
    private String currency;
    private String productUrl;
    private String imageUrl;

    // Getters and Setters
    public String getSiteName() {
        return siteName;
    }
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getProductUrl() {
        return productUrl;
    }
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
