package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockOwnedTest {

    private StockOwned stockOwned;

    private static final String STOCK_SYMBOL = "GOOG";
    private static final int NUM_SHARES_OWNED = 10;
    private static final double AVERAGE_COST = 2800.00;

    @BeforeEach
    void runBefore() {
        stockOwned = new StockOwned(STOCK_SYMBOL, NUM_SHARES_OWNED, AVERAGE_COST);
    }

    @Test
    void testConstructor() {
        assertEquals(STOCK_SYMBOL, stockOwned.getStockSymbol());
        assertEquals(NUM_SHARES_OWNED, stockOwned.getNumSharesOwned());
        assertEquals(AVERAGE_COST, stockOwned.getAverageCost());
    }

    @Test
    void testBuyStockOwned() {
        stockOwned.buyStockOwned(10, 3000.00);
        assertEquals(10 + NUM_SHARES_OWNED, stockOwned.getNumSharesOwned());
        assertEquals((3000 * 10 + AVERAGE_COST * NUM_SHARES_OWNED) / (10 + NUM_SHARES_OWNED),
                stockOwned.getAverageCost());
    }

    @Test
    void testSellStockOwned() {
        stockOwned.sellStockOwned((int) NUM_SHARES_OWNED / 2);
        assertEquals(NUM_SHARES_OWNED - (int) NUM_SHARES_OWNED / 2, stockOwned.getNumSharesOwned());
    }

    @Test
    void testTotalValueAtAverageCost() {
        assertEquals(NUM_SHARES_OWNED * AVERAGE_COST, stockOwned.totalValueAtAverageCost());
    }

}
