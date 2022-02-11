package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {

    private Portfolio portfolio;

    private static final Stock APPLE = new Stock("AAPL", "Apple Inc.", 176.28, 0.0005,
            16320000000L, 6.01, 1.19, "Technology");
    private static final int NUM_SHARES_TO_BUY = 20;
    private static final double PRICE = 200.00;
    private static final StockOwned APPLE_OWNED = new StockOwned(APPLE, NUM_SHARES_TO_BUY, PRICE);

    private static final Stock MICROSOFT = new Stock("MSFT", "Microsoft Corporation", 311.21,
            0.0003, 7500000000L, 9.39, 0.89, "Technology");
    private static final StockOwned MICROSOFT_OWNED = new StockOwned(MICROSOFT, NUM_SHARES_TO_BUY, PRICE);

    @BeforeEach
    void runBefore() {
        portfolio = new Portfolio();
    }

    @Test
    void testConstructor() {
        assertEquals(0, portfolio.size());
        assertTrue(portfolio.isEmpty());
    }

    @Test
    void testAddStockOwned() {
        portfolio.addStockOwned(APPLE_OWNED);
        assertEquals(1, portfolio.size());
        assertEquals(APPLE_OWNED, portfolio.getStock(0));
    }

    @Test
    void testRemoveStock() {
        portfolio.addStockOwned(APPLE_OWNED);
        portfolio.removeStockOwned(APPLE_OWNED);
        assertEquals(0, portfolio.size());
        assertTrue(portfolio.isEmpty());
    }

    @Test
    void testGetStockSymbolNull() {
        assertEquals(null, portfolio.getStock(APPLE_OWNED.getStock().getSymbol()));
    }

    @Test
    void testGetStockSymbolSuccess() {
        portfolio.addStockOwned(APPLE_OWNED);
        portfolio.addStockOwned(MICROSOFT_OWNED);
        assertEquals(MICROSOFT_OWNED, portfolio.getStock(MICROSOFT_OWNED.getStock().getSymbol()));
    }

    @Test
    void testGetStockIndex() {
        portfolio.addStockOwned(APPLE_OWNED);
        portfolio.addStockOwned(MICROSOFT_OWNED);
        assertEquals(MICROSOFT_OWNED, portfolio.getStock(1));
    }

    @Test
    void testTotalPortfolioMarketValue() {
        portfolio.addStockOwned(APPLE_OWNED);
        portfolio.addStockOwned(MICROSOFT_OWNED);
        assertEquals(APPLE_OWNED.totalValueAtMarketPrice() + MICROSOFT_OWNED.totalValueAtMarketPrice(),
                portfolio.totalPortfolioMarketValue());
    }

    @Test
    void testTotalPortfolioMarketValueZero() {
        assertEquals(0,portfolio.totalPortfolioMarketValue());
    }

}
