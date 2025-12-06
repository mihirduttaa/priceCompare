package com.pricecompare.price_compare.repository;

import com.pricecompare.price_compare.entity.Offer;
import com.pricecompare.price_compare.entity.Product;
import com.pricecompare.price_compare.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByProductOrderByPriceAsc(Product product);

    List<Offer> findByProductAndSource(Product product, Source source);
}
