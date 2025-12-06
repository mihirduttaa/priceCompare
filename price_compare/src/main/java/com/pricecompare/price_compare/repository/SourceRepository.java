package com.pricecompare.price_compare.repository;

import com.pricecompare.price_compare.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SourceRepository extends JpaRepository<Source, Long> {
    Optional<Source> findByNameIgnoreCase(String name);
}
