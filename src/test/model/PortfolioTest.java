package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {

    private Portfolio portfolio;

    private static final int NUM_SHARES_TO_BUY = 20;
    private static final double PRICE = 200.00;
    private static final StockOwned APPLE_OWNED = new StockOwned("AAPL", NUM_SHARES_TO_BUY, PRICE);
    private static final StockOwned MICROSOFT_OWNED = new StockOwned("MSFT", NUM_SHARES_TO_BUY, PRICE);

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
        assertEquals(null, portfolio.getStock("AAPL"));
    }

    @Test
    void testGetStockSymbolSuccess() {
        portfolio.addStockOwned(APPLE_OWNED);
        portfolio.addStockOwned(MICROSOFT_OWNED);
        assertEquals(MICROSOFT_OWNED, portfolio.getStock("MSFT"));
    }

    @Test
    void testGetStockIndex() {
        portfolio.addStockOwned(APPLE_OWNED);
        portfolio.addStockOwned(MICROSOFT_OWNED);
        assertEquals(MICROSOFT_OWNED, portfolio.getStock(1));
    }

}
