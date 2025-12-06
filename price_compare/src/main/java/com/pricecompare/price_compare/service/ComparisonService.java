package com.pricecompare.price_compare.service;

import com.pricecompare.price_compare.dto.ProductComparisonDto;
import com.pricecompare.price_compare.dto.ScrapedOfferDto;
import com.pricecompare.price_compare.entity.Offer;
import com.pricecompare.price_compare.entity.Product;
import com.pricecompare.price_compare.repository.OfferRepository;
import com.pricecompare.price_compare.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComparisonService {
    private final ScraperService scraperService;
    private final ImportService importService;
    private final ProductRepository productRepository;
    private final OfferRepository offerRepository;

    public ComparisonService(ScraperService scraperService,
            ImportService importService,
            ProductRepository productRepository,
            OfferRepository offerRepository) {
        this.scraperService = scraperService;
        this.importService = importService;
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
    }

    public ProductComparisonDto compare(String query) throws Exception {
        // 1) scrape all sources
        List<ScrapedOfferDto> scraped = scraperService.scrapeAll(query);

        // 2) save to DB
        importService.saveScrapedOffers(query, scraped);

        // 3) load product + offers from DB
        Product product = productRepository.findByNameContainingIgnoreCase(query)
                .stream().findFirst().orElseThrow();

        List<Offer> offers = offerRepository.findByProductOrderByPriceAsc(product);
        offers.sort(Comparator.comparing(Offer::getPrice));

        // 4) map to DTO
        ProductComparisonDto dto = new ProductComparisonDto();
        dto.setQuery(query);
        dto.setProductName(product.getName());

        List<ProductComparisonDto.OfferDto> offerDtos = offers.stream().map(o -> {
            ProductComparisonDto.OfferDto x = new ProductComparisonDto.OfferDto();
            x.setSourceName(o.getSource().getName());
            x.setPrice(o.getPrice());
            x.setCurrency(o.getCurrency());
            x.setProductUrl(o.getProductUrl());
            // x.setImageUrl(o.getImageUrl());
            return x;
        }).collect(Collectors.toList());

        dto.setOffers(offerDtos);
        return dto;
    }
}
