package com.pricecompare.price_compare.service;

import com.pricecompare.price_compare.dto.ScrapedOfferDto;
import com.pricecompare.price_compare.entity.Offer;
import com.pricecompare.price_compare.entity.Product;
import com.pricecompare.price_compare.entity.Source;
import com.pricecompare.price_compare.repository.OfferRepository;
import com.pricecompare.price_compare.repository.ProductRepository;
import com.pricecompare.price_compare.repository.SourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ImportService {
    private final ProductRepository productRepository;
    private final SourceRepository sourceRepository;
    private final OfferRepository offerRepository;

    public ImportService(ProductRepository productRepository,
            SourceRepository sourceRepository,
            OfferRepository offerRepository) {
        this.productRepository = productRepository;
        this.sourceRepository = sourceRepository;
        this.offerRepository = offerRepository;
    }

    @Transactional
    public void saveScrapedOffers(String query, List<ScrapedOfferDto> scrapedOffers) {
        // For MVP, treat the query as product name
        Product product = findOrCreateProduct(query);

        for (ScrapedOfferDto dto : scrapedOffers) {
            Source source = findOrCreateSource(dto.getSiteName(), dto.getProductUrl());

            Offer offer = new Offer();
            offer.setProduct(product);
            offer.setSource(source);
            offer.setTitle(dto.getTitle());
            offer.setPrice(dto.getPrice());
            offer.setCurrency(dto.getCurrency());
            offer.setProductUrl(dto.getProductUrl());
            // offer.setImageUrl(dto.getImageUrl()); // add field in entity if missing
            offer.setLastCheckedAt(OffsetDateTime.now());

            offerRepository.save(offer);
        }
    }

    private Product findOrCreateProduct(String name) {
        List<Product> existing = productRepository.findByNameContainingIgnoreCase(name);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }
        Product p = new Product();
        p.setName(name);
        return productRepository.save(p);
    }

    private Source findOrCreateSource(String name, String baseUrl) {
        Optional<Source> existing = sourceRepository.findByNameIgnoreCase(name);
        if (existing.isPresent()) {
            return existing.get();
        }
        Source s = new Source();
        s.setName(name);
        s.setBaseUrl(baseUrl);
        return sourceRepository.save(s);
    }
}
