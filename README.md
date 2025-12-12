# PriceCompare

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

```text
src/main/java/com/pricecompare/price_compare
├── config
│   └── CorsConfig.java           # CORS for web frontends
├── controller
│   ├── ComparisonController.java # Public REST API (/api/v1/compare)
│   └── ScraperController.java    # Debug endpoints (optional)
├── dto
│   ├── ProductComparisonDto.java # API response model
│   └── ScrapedOfferDto.java      # Internal scraped data model
├── entity
│   ├── Product.java
│   ├── Offer.java
│   └── Source.java
├── repository
│   ├── ProductRepository.java
│   ├── OfferRepository.java
│   └── SourceRepository.java
├── service
│   ├── ScraperService.java       # Scrapes demo/catalog sites
│   ├── ImportService.java        # Saves scraped data into DB
│   └── ComparisonService.java    # Builds comparison response DTO
└── PriceCompareApplication.java  # Spring Boot main class
```
## 3. API Contract (for Frontend)

### 3.1 Base URL and Headers

- Local base URL: `http://localhost:8080`  
- All APIs are under base path: `/api/v1`  
- Content type: all requests and responses use `application/json`.  
- CORS: configured to allow local web and Android development environments. [web:94]

### 3.2 Main Endpoint – Compare Prices

This is the primary endpoint the frontend will use in the app.

- Method: `POST`  
- URL: `/api/v1/compare`  
- Purpose: given a product name, returns offers from multiple sources.

#### 3.2.1 Request Body
```text
{
"productName": "Apple iPhone 15 128GB",
"maxResults": 10
}
```
- `productName` (string, required): Search phrase or product name entered by the user.  
- `maxResults` (integer, optional): Maximum number of offers to return; default is 10 if omitted.

#### 3.2.2 Successful Response (200)
```text
{
"productName": "Apple iPhone 15 128GB",
"offers": [
{
"sourceName": "DemoStore A",
"productTitle": "Apple iPhone 15 128GB (Black)",
"price": 79999.0,
"currency": "INR",
"productUrl": "https://demostore-a.example.com/iphone-15-128-black",
"inStock": true,
"lastUpdated": "2025-12-12T10:15:30Z"
},
{
"sourceName": "DemoStore B",
"productTitle": "Apple iPhone 15 128GB",
"price": 78999.0,
"currency": "INR",
"productUrl": "https://demostore-b.example.com/iphone-15-128",
"inStock": true,
"lastUpdated": "2025-12-12T10:16:02Z"
}
]
}
```

- `productName` (string): Echo of the requested product.  
- `offers` (array):
  - `sourceName` (string): Identifier of the source.  
  - `productTitle` (string): Title as scraped from the source page.  
  - `price` (number): Final price of the product.  
  - `currency` (string): Currency code, e.g. `INR`.  
  - `productUrl` (string): Link to open in a browser or WebView.  
  - `inStock` (boolean): Availability flag.  
  - `lastUpdated` (string, ISO‑8601): Timestamp when this offer was last refreshed.  

Offers are sorted from lowest price to highest.

### 3.3 Error Model

Errors are returned using a consistent JSON structure.
```text
{
"timestamp": "2025-12-12T10:20:00Z",
"status": 400,
"error": "Bad Request",
"message": "productName must not be empty",
"path": "/api/v1/compare"
}
```

- **400 Bad Request**: Validation issues, such as missing or empty `productName`.  
- **500 Internal Server Error**: Unexpected errors or scraper failures.  
- Frontend should show user‑friendly messages based on `message` and may log `status` and `error`.

### 3.4 Debug / Scraper Endpoints

These are for internal testing and should not be used in production UI.

- Controller: `ScraperController`  
- Sample purpose: manually trigger scrapes or inspect raw scraped data.  
- Frontend teams should confirm with backend before using these in any tools.

---

## 4. Integration Examples

### 4.1 Web (React with Axios)
```text
import axios from "axios";

const BASE_URL = "http://localhost:8080/api/v1";

export async function comparePrices(productName: string, maxResults = 10) {
const res = await axios.post(${BASE_URL}/compare, {
productName,
maxResults
});
return res.data; // { productName, offers: [...] }
}
```

### 4.2 Android (Kotlin + Retrofit)
```text
data class CompareRequest(
val productName: String,
val maxResults: Int = 10
)

data class OfferDto(
val sourceName: String,
val productTitle: String,
val price: Double,
val currency: String,
val productUrl: String,
val inStock: Boolean,
val lastUpdated: String
)

data class ProductComparisonDto(
val productName: String,
val offers: List<OfferDto>
)

interface PriceCompareApi {
@POST("/api/v1/compare")
suspend fun comparePrices(@Body body: CompareRequest): ProductComparisonDto
}
```

Frontend teams can plug these examples directly into their networking layer and build UI lists against the `offers` array fields.

---

## 5. Local Setup (Backend)

### 5.1 Prerequisites

- Java 17+  
- Maven (or Maven wrapper included)  
- PostgreSQL running locally  

### 5.2 Create Database and User (PostgreSQL)

Connect as `postgres` (or another superuser):
```text
psql -U postgres
```

Inside `psql`:
```text
CREATE DATABASE price_comparison;

CREATE ROLE "priceCompareUser" WITH LOGIN PASSWORD 'your_password_here';

GRANT ALL PRIVILEGES ON DATABASE price_comparison TO "priceCompareUser";

\c price_comparison;

GRANT USAGE, CREATE ON SCHEMA public TO "priceCompareUser";
ALTER SCHEMA public OWNER TO "priceCompareUser";

\q
```

### 5.3 Configure Spring Boot

Edit `src/main/resources/application.properties`:
```text
spring.datasource.url=jdbc:postgresql://localhost:5432/price_comparison
spring.datasource.username=priceCompareUser
spring.datasource.password=your_password_here

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```
If your database name or user differs, adjust the URL, username, and password accordingly
