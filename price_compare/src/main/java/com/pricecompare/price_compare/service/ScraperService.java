package com.pricecompare.price_compare.service;

import com.pricecompare.price_compare.dto.ScrapedOfferDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
// import java.net.URLEncoder;
// import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";

    /**
     * Scrape one e‑commerce site for a search query.
     * Later you can create one method per site with different selectors.
     */
    public List<ScrapedOfferDto> scrapeAmazonMock(String query) throws Exception {
        // String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String searchUrl = "https://books.toscrape.com/";

        Document doc = Jsoup.connect(searchUrl)
                .userAgent(USER_AGENT)
                .timeout(10_000)
                .get();

        List<ScrapedOfferDto> results = new ArrayList<>();

        // Example selectors – change to match real site HTML
        Elements productCards = doc.select("article.product_pod");

        for (Element card : productCards) {
            String title = card.select("h3 a").attr("title");
            String priceText = card.select("p.price_color").text();
            String productUrl = card.select("h3 a").attr("abs:href");
            String imageUrl = card.select("div.image_container img").attr("abs:src");

            BigDecimal price = parsePrice(priceText);
            if (title.isBlank() || price == null || productUrl.isBlank()) {
                continue;
            }

            ScrapedOfferDto dto = new ScrapedOfferDto();
            dto.setSiteName("AmazonMock");
            dto.setTitle(title);
            dto.setPrice(price);
            dto.setCurrency("INR");
            dto.setProductUrl(productUrl);
            dto.setImageUrl(imageUrl);

            results.add(dto);
        }

        return results;
    }

    public List<ScrapedOfferDto> scrapeFlipkartMock(String query) throws Exception {
        // You can point this to another demo/catalog site and adjust selectors
        return scrapeAmazonMock(query);
    }

    public List<ScrapedOfferDto> scrapeAll(String query) throws Exception {
        List<ScrapedOfferDto> all = new ArrayList<>();
        all.addAll(scrapeAmazonMock(query));
        all.addAll(scrapeFlipkartMock(query));
        return all;
    }

    private BigDecimal parsePrice(String raw) {
        if (raw == null)
            return null;
        // Remove currency symbols and commas, keep digits + dot
        String cleaned = raw.replaceAll("[^0-9.]", "");
        if (cleaned.isEmpty())
            return null;
        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
