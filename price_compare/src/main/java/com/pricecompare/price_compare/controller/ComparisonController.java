package com.pricecompare.price_compare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricecompare.price_compare.dto.ProductComparisonDto;
import com.pricecompare.price_compare.service.ComparisonService;

@RestController
@RequestMapping("/api/v1")
public class ComparisonController {
    private final ComparisonService comparisonService;

    public ComparisonController(ComparisonService comparisonService) {
        this.comparisonService = comparisonService;
    }

    @GetMapping("/compare")
    public ResponseEntity<?> compare(@RequestParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Query must not be empty");
        }
        try {
            ProductComparisonDto dto = comparisonService.compare(query);
            if (dto.getOffers() == null || dto.getOffers().isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            // In real app: log this error
            return ResponseEntity.status(500).body("Internal error: " + e.getMessage());
        }
    }
}
