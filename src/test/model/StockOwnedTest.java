package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockOwnedTest {

    private StockOwned stockOwned;

    private static final Stock GOOGLE = new Stock("GOOG", "Alphabet Inc.", 2829.06,
            0.0007, 315640000L, 112.2, 1.07, "Technology");
    private static final int NUM_SHARES_OWNED = 10;
    private static final double AVERAGE_COST = 2800.00;

    @BeforeEach
    void runBefore() {
        stockOwned = new StockOwned(GOOGLE, NUM_SHARES_OWNED, AVERAGE_COST);
    }

    @Test
    void testConstructor() {
        assertEquals(GOOGLE, stockOwned.getStock());
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

    @Test
    void testTotalValueAtMarketPrice() {
        assertEquals(NUM_SHARES_OWNED * stockOwned.getStock().marketPrice(),
                stockOwned.totalValueAtMarketPrice());
    }

    @Test
    void testProfitOrLoss() {
        assertEquals(NUM_SHARES_OWNED * (stockOwned.getStock().marketPrice() - stockOwned.getAverageCost()),
                stockOwned.profitOrLoss());
    }

}
