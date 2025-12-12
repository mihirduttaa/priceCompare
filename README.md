# PriceCompare – Backend

Interactive backend service to compare prices of a product across multiple e‑commerce sources (currently demo/mock scrapers).  
Frontend can be Android (Kotlin/Java) or Web (React) consuming a simple REST API.

---

## 1. Tech Stack

- Language: Java 17
- Framework: Spring Boot
- Build: Maven
- Database: PostgreSQL
- HTML parsing / scraping: Jsoup

---

## 2. Project Structure (Backend)

src/main/java/com/pricecompare/price_compare
├── config
│ └── CorsConfig.java # CORS for web frontends
├── controller
│ ├── ComparisonController.java # Public REST API (/api/v1/compare)
│ └── ScraperController.java # Debug endpoints (optional)
├── dto
│ ├── ProductComparisonDto.java # API response model
│ └── ScrapedOfferDto.java # Internal scraped data model
├── entity
│ ├── Product.java
│ ├── Offer.java
│ └── Source.java
├── repository
│ ├── ProductRepository.java
│ ├── OfferRepository.java
│ └── SourceRepository.java
├── service
│ ├── ScraperService.java # Scrapes demo/catalog sites
│ ├── ImportService.java # Saves scraped data into DB
│ └── ComparisonService.java # Builds comparison response DTO
└── PriceCompareApplication.java # Spring Boot main class

---

## 3. Local Setup

### 3.1 Prerequisites

- Java 17+
- Maven (or Maven wrapper included)
- PostgreSQL running locally

### 3.2 Create database and user (PostgreSQL)

Connect as `postgres` (or another superuser):

psql -U postgres

Inside `psql`:

CREATE DATABASE price_comparison;

CREATE ROLE "priceCompareUser" WITH LOGIN PASSWORD 'your_password_here';

GRANT ALL PRIVILEGES ON DATABASE price_comparison TO "priceCompareUser";

\c price_comparison;

GRANT USAGE, CREATE ON SCHEMA public TO "priceCompareUser";
ALTER SCHEMA public OWNER TO "priceCompareUser";

\q


### 3.3 Configure Spring Boot

Edit `src/main/resources/application.properties`:

spring.datasource.url=jdbc:postgresql://localhost:5432/price_comparison
spring.datasource.username=priceCompareUser
spring.datasource.password=your_password_here

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080


(If your DB user or DB name differs, adjust the URL/username/password.)

### 3.4 Run the backend

From the project root:

./mvnw spring-boot:run

Backend will start on:

http://localhost:8080


---

## 4. Public REST API (for Frontend)

### Base URL

- Local dev: `http://localhost:8080`
- All endpoints below are relative to this base.

### 4.1 Compare prices for a query

**Endpoint**

`GET /api/v1/compare`

**Description**

Scrapes configured sources for the given query, stores the results, and returns a comparison response with offers sorted by price.

**Query parameters**

| Name | Type   | Required | Description                               |
|------|--------|----------|-------------------------------------------|
| q    | string | yes      | Search text, e.g. `"iphone 15 128GB"`     |

**Example request**

GET /api/v1/compare?q=iphone%2015%20128GB


**Success response – 200 OK**

{
"query": "iphone 15 128GB",
"productName": "iphone 15 128GB",
"offers": [
{
"sourceName": "AmazonMock",
"title": "iPhone 15 128GB Blue",
"price": 69999.0,
"currency": "INR",
"productUrl": "https://example.com/product/123",
"imageUrl": "https://example.com/images/123.jpg"
},
{
"sourceName": "FlipkartMock",
"title": "iPhone 15 128GB Black",
"price": 68999.0,
"currency": "INR",
"productUrl": "https://example.com/product/456",
"imageUrl": "https://example.com/images/456.jpg"
}
]
}


**Field descriptions**

- `query` – Original query the client sent.
- `productName` – Normalized product name stored by backend.
- `offers` – Array of offers from different sources:
  - `sourceName` – Source/site name (e.g. `"Amazon"`, `"Flipkart"`).
  - `title` – Product title from that source.
  - `price` – Price as decimal (no currency symbol).
  - `currency` – Currency code, e.g. `"INR"`.
  - `productUrl` – URL for opening in WebView / browser.
  - `imageUrl` – Optional product image URL (may be null/empty).

**Error responses**

- `400 Bad Request` – missing or blank `q`:

{ "error": "Query must not be empty" }

- `404 Not Found` – no offers found for the query:

{ "error": "No offers found" }

- `500 Internal Server Error` – unexpected backend/scraper error:

{ "error": "Internal error: <message>" }

---

## 5. CORS (for React / Web)

Web frontend (React dev server) calls from `http://localhost:3000` are allowed via `CorsConfig`:

registry.addMapping("/api/**")
.allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000")


