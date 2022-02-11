package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {

    private Stock stock;

    @BeforeEach
    void runBefore() {
        stock = new Stock("TSLA", "Tesla, Inc.", 932.00, 0.0008,
                1030000000L, 4.9, 2.01, "Capital Goods");
    }

    @Test
    void testConstructor() {
        assertEquals("TSLA", stock.getSymbol());
        assertEquals("Tesla, Inc.", stock.getName());
        assertEquals(932.00, stock.getAskPrice());
        assertEquals(0.0008, stock.getBidAskSpread());
        assertEquals(1030000000L, stock.getTotalNumShares());
        assertEquals(4.9, stock.getEarningsPerShare());
        assertEquals(2.01, stock.getBeta());
        assertEquals("Capital Goods", stock.getSector());
        assertEquals(Stock.DEFAULT_PRICE_MID, stock.getPriceMidForRand());
        assertEquals(Stock.DEFAULT_PRICE_UP, stock.getPriceUpForRand());
        assertEquals(Stock.DEFAULT_PRICE_DOWN, stock.getPriceDownForRand());
        assertEquals(Stock.DEFAULT_SPREAD_MID, stock.getSpreadMidForRand());
        assertEquals(Stock.DEFAULT_SPREAD_UP, stock.getSpreadUpForRand());
        assertEquals(Stock.DEFAULT_SPREAD_DOWN, stock.getSpreadDownForRand());
    }

    @Test
    void testMarketPrice() {
        assertEquals((stock.getBidPrice() + stock.getAskPrice()) / 2, stock.marketPrice());
    }

    @Test
    void testpeRatio() {
        assertEquals(stock.marketPrice() / stock.getEarningsPerShare(), stock.peRatio());
    }

    @Test
    void testMarketCap() {
        assertEquals(stock.marketPrice() * stock.getTotalNumShares(), stock.marketCap());
    }

    @Test
    void testRandPriceforPrice() {
        int priceScaledLowerBound = Stock.DEFAULT_PRICE_MID - (int) (Stock.DEFAULT_PRICE_DOWN * stock.getBeta());
        int priceFactorLowerBound = priceScaledLowerBound;
        int priceFactorUpperBound = (int) ((Stock.DEFAULT_PRICE_DOWN * Stock.DEFAULT_PRICE_UP) * stock.getBeta())
                + priceScaledLowerBound;
        double askPriceLowerBound = stock.getAskPrice() * priceFactorLowerBound / Stock.DEFAULT_PRICE_MID;
        double askPriceUpperBound = stock.getAskPrice() * priceFactorUpperBound / Stock.DEFAULT_PRICE_MID;

        double testAskPriceUpperBound = stock.getAskPrice();
        double testAskPriceLowerBound = stock.getAskPrice();

        for (int i = 1; i < 1000; i++) {
            stock = new Stock("TSLA", "Tesla, Inc.", 932.00, 0.0008,
                    1030000000L, 4.9, 2.01, "Capital Goods");
            stock.randPrice();
            if (stock.getAskPrice() > testAskPriceUpperBound) {
                testAskPriceUpperBound = stock.getAskPrice();
            } else if (stock.getAskPrice() < testAskPriceLowerBound) {
                testAskPriceLowerBound = stock.getAskPrice();
            }
        }
        assertTrue(askPriceLowerBound <= stock.getAskPrice());
        assertTrue(askPriceUpperBound >= stock.getAskPrice());
        assertEquals(stock.getAskPrice() * (1 - stock.getBidAskSpread()), stock.getBidPrice());
    }

    @Test
    void testRandPriceForSpread() {
        int spreadScaledLowerBound = Stock.DEFAULT_SPREAD_MID - (int) (Stock.DEFAULT_SPREAD_DOWN * stock.getBeta());
        int spreadFactorLowerBound = spreadScaledLowerBound;
        int spreadFactorUpperBound = (int) ((Stock.DEFAULT_SPREAD_DOWN * Stock.DEFAULT_SPREAD_UP) * stock.getBeta())
                + spreadScaledLowerBound;
        double spreadLowerBound = stock.getBidAskSpread() * spreadFactorLowerBound / Stock.DEFAULT_SPREAD_MID;
        double spreadUpperBound = stock.getBidAskSpread() * spreadFactorUpperBound / Stock.DEFAULT_SPREAD_MID;

        double testSpreadUpperBound = stock.getBidAskSpread();
        double testSpreadLowerBound = stock.getBidAskSpread();

        for (int i = 1; i < 1000; i++) {
            stock = new Stock("TSLA", "Tesla, Inc.", 932.00, 0.0008,
                    1030000000L, 4.9, 2.01, "Capital Goods");
            stock.randPrice();
            if (stock.getBidAskSpread() > testSpreadUpperBound) {
                testSpreadUpperBound = stock.getBidAskSpread();
            } else if (stock.getBidAskSpread() < testSpreadLowerBound) {
                testSpreadLowerBound = stock.getBidAskSpread();
            }
        }
        assertTrue(spreadLowerBound <= stock.getBidAskSpread());
        assertTrue(spreadUpperBound >= stock.getBidAskSpread());
    }

    @Test
    void testSetTotalNumShares() {
        stock.setTotalNumShares(10);
        assertEquals(10,stock.getTotalNumShares());
    }

    @Test
    void testSetEarningsPerShare() {
        stock.setEarningsPerShare(0);
        assertEquals(0,stock.getEarningsPerShare());
    }

    @Test
    void testSetBeta() {
        stock.setBeta(0.3);
        assertEquals(0.3,stock.getBeta());
    }

    @Test
    void testSetPriceMidForRand() {
        stock.setPriceMidForRand(20000);
        assertEquals(20000,stock.getPriceMidForRand());
    }

    @Test
    void testSetPriceUpForRand() {
        stock.setPriceUpForRand(10);
        assertEquals(10,stock.getPriceUpForRand());
    }

    @Test
    void testSetPriceDownForRand() {
        stock.setPriceDownForRand(20);
        assertEquals(20,stock.getPriceDownForRand());
    }

    @Test
    void testSetSpreadMidForRand() {
        stock.setSpreadMidForRand(12000);
        assertEquals(12000,stock.getSpreadMidForRand());
    }

    @Test
    void testSetSpreadUpForRand() {
        stock.setSpreadUpForRand(30);
        assertEquals(30,stock.getSpreadUpForRand());
    }

    @Test
    void testSetSpreadDownForRand() {
        stock.setSpreadDownForRand(15);
        assertEquals(15,stock.getSpreadDownForRand());
    }

}
