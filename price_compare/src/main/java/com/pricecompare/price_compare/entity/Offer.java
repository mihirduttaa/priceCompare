package com.pricecompare.price_compare.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source source;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 3)
    private String currency = "INR";

    @Column(name = "product_url", nullable = false)
    private String productUrl;

    private String availability; // e.g. IN_STOCK, OUT_OF_STOCK

    private BigDecimal rating; // 0–5, nullable

    @Column(name = "num_reviews")
    private Integer numReviews;

    @Column(name = "last_checked_at")
    private OffsetDateTime lastCheckedAt = OffsetDateTime.now();

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
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

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(Integer numReviews) {
        this.numReviews = numReviews;
    }

    public OffsetDateTime getLastCheckedAt() {
        return lastCheckedAt;
    }

    public void setLastCheckedAt(OffsetDateTime lastCheckedAt) {
        this.lastCheckedAt = lastCheckedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
