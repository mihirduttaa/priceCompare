import React, { useState } from "react";

function App() {
  const [query, setQuery] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) {
      setError("Please enter a product name.");
      return;
    }
    setError("");
    setLoading(true);
    setResult(null);

    try {
      const res = await fetch(
        `http://localhost:8080/api/compare?q=${encodeURIComponent(query)}`
      );
      if (!res.ok) {
        throw new Error(`Backend error: ${res.status}`);
      }
      const data = await res.json();
      setResult(data);
    } catch (err) {
      setError(err.message || "Something went wrong");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 900, margin: "40px auto", fontFamily: "sans-serif" }}>
      <h1>Price Comparison</h1>

      <form onSubmit={handleSearch} style={{ marginBottom: 20 }}>
        <input
          type="text"
          placeholder="Search product, e.g. iPhone"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          style={{ padding: 8, width: "70%", marginRight: 8 }}
        />
        <button type="submit" style={{ padding: "8px 16px" }}>
          Compare
        </button>
      </form>

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {result && (
        <div>
          <h2>
            Results for: <em>{result.productName || result.query}</em>
          </h2>
          {(!result.offers || result.offers.length === 0) ? (
            <p>No offers found.</p>
          ) : (
            <table
              style={{
                borderCollapse: "collapse",
                width: "100%",
                marginTop: 16,
              }}
            >
              <thead>
                <tr>
                  <th style={{ border: "1px solid #ccc", padding: 8 }}>Source</th>
                  <th style={{ border: "1px solid #ccc", padding: 8 }}>Title</th>
                  <th style={{ border: "1px solid #ccc", padding: 8 }}>Price</th>
                  <th style={{ border: "1px solid #ccc", padding: 8 }}>Link</th>
                </tr>
              </thead>
              <tbody>
                {result.offers.map((offer, idx) => (
                  <tr key={idx}>
                    <td style={{ border: "1px solid #ccc", padding: 8 }}>
                      {offer.sourceName}
                    </td>
                    <td style={{ border: "1px solid #ccc", padding: 8 }}>
                      {offer.title}
                    </td>
                    <td style={{ border: "1px solid #ccc", padding: 8 }}>
                      {offer.price} {offer.currency}
                    </td>
                    <td style={{ border: "1px solid #ccc", padding: 8 }}>
                      <a href={offer.productUrl} target="_blank" rel="noreferrer">
                        View
                      </a>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}

export default App;
