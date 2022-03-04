package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {

    private Market market;
    private static final Stock APPLE = new Stock("AAPL", "Apple Inc.", 176.28, 0.0005,
            16320000000L, 6.01, 1.19, "Technology");
    private static final Stock MICROSOFT = new Stock("MSFT", "Microsoft Corporation", 311.21,
            0.0003, 7500000000L, 9.39, 0.89, "Technology");

    @BeforeEach
    void runBefore() {
        market = new Market();
    }

    @Test
    void testConstructor() {
        assertEquals(0, market.size());
        assertTrue(market.isEmpty());
    }

    @Test
    void testAddStock() {
        market.addStock(APPLE);
        assertEquals(APPLE, market.getStock("AAPL"));
        assertEquals(APPLE, market.getStock(0));
        assertEquals(1, market.size());
        assertFalse(market.isEmpty());
    }

    @Test
    void testGetStockSymbolNull() {
        assertEquals(null, market.getStock("AAPL"));
    }

    @Test
    void testGetStockSymbolSuccess() {
        market.addStock(APPLE);
        market.addStock(MICROSOFT);
        assertEquals(MICROSOFT, market.getStock("MSFT"));
    }

    @Test
    void testGetStockIndex() {
        market.addStock(APPLE);
        market.addStock(MICROSOFT);
        assertEquals(MICROSOFT, market.getStock(1));
    }

    @Test
    void testStockValueAtMarketPrice() {
        market.addStock(APPLE);
        assertEquals(APPLE.marketPrice() * 10,
                market.stockValueAtMarketPrice(APPLE.getSymbol(), 10));
    }

}
