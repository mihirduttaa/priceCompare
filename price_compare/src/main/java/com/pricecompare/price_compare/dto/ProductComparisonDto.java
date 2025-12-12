package com.pricecompare.price_compare.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductComparisonDto {
    private String query;
    private String productName;
    private List<OfferDto> offers;

    public static class OfferDto {
        private String sourceName;
        private String title;
        private BigDecimal price;
        private String currency;
        private String productUrl;
        private String imageUrl;

        // getters/setters
        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    // getters/setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<OfferDto> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferDto> offers) {
        this.offers = offers;
    }
}
