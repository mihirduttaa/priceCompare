package com.pricecompare.price_compare.controller;

import com.pricecompare.price_compare.dto.ScrapedOfferDto;
import com.pricecompare.price_compare.service.ScraperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScraperController {
    private final ScraperService scraperService;

    public ScraperController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping("/api/debug/price-compare")
    public List<ScrapedOfferDto> scrapeOffer(@RequestParam String q) throws Exception {
        return scraperService.scrapeAll(q);
    }
}
