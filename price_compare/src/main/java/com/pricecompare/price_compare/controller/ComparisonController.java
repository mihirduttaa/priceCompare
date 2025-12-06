package com.pricecompare.price_compare.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricecompare.price_compare.dto.ProductComparisonDto;
import com.pricecompare.price_compare.service.ComparisonService;

@RestController
public class ComparisonController {
    private final ComparisonService comparisonService;

    public ComparisonController(ComparisonService comparisonService) {
        this.comparisonService = comparisonService;
    }

    @GetMapping("/api/compare")
    public ProductComparisonDto compare(@RequestParam String q) throws Exception {
        return comparisonService.compare(q);
    }
}
