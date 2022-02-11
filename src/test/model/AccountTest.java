package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    private static final String USERNAME = "dom";
    private static final String PASSWORD = "123";
    private static final String NAME = "Dominic";
    private static final Stock MICROSOFT = new Stock("MSFT", "Microsoft Corporation", 311.21,
            0.0003, 7500000000L, 9.39, 0.89, "Technology");
    private static final Stock APPLE = new Stock("AAPL", "Apple Inc.", 176.28, 0.0005,
            16320000000L, 6.01, 1.19, "Technology");

    @BeforeEach
    void runBefore() {
        account = new Account(USERNAME, PASSWORD, NAME);
    }

    @Test
    void testConstructor() {
        assertEquals(USERNAME, account.getUsername());
        assertEquals(PASSWORD, account.getPassword());
        assertEquals(NAME, account.getName());
        assertEquals(0, account.getBalance());
        assertEquals(Account.DEFAULT_FEE_PER_TRADE, account.getFeePerTrade());
        assertTrue(account.getPortfolio().isEmpty());
    }

    @Test
    void testDeposit() {
        account.deposit(100);
        assertEquals(100, account.getBalance());
    }

    @Test
    void testWithdraw() {
        account.deposit(100);
        account.withdraw(20);
        assertEquals(80, account.getBalance());
    }

    @Test
    void testBuyStockNotYetOwned() {
        account.deposit(10000);
        account.buy(MICROSOFT, 10, 500);
        assertEquals(10000 - 10 * 500 - Account.DEFAULT_FEE_PER_TRADE, account.getBalance());
        assertEquals(1, account.getPortfolio().size());
        assertEquals(MICROSOFT, account.getPortfolio().getStock(0).getStock());
        assertEquals(10, account.getPortfolio().getStock(0).getNumSharesOwned());
        assertEquals(500, account.getPortfolio().getStock(0).getAverageCost());
    }


    @Test
    void testBuyStockAlreadyOwned() {
        account.deposit(20000);
        account.buy(MICROSOFT, 10, 500);
        account.buy(MICROSOFT, 10, 600);
        assertEquals(10 + 10, account.getPortfolio().getStock(0).getNumSharesOwned());
        assertEquals((10 * 500 + 10 * 600) / (10 + 10), account.getPortfolio().getStock(0).getAverageCost());
        assertEquals(1, account.getPortfolio().size());
    }

    @Test
    void testBuyTwoStocksNotYetOwned() {
        account.deposit(10000);
        account.buy(MICROSOFT, 10, 500);
        account.buy(APPLE, 20, 200);
        assertEquals(10000 - 10 * 500 - 20 * 200 - Account.DEFAULT_FEE_PER_TRADE * 2, account.getBalance());
        assertEquals(2, account.getPortfolio().size());
        assertEquals(MICROSOFT, account.getPortfolio().getStock(0).getStock());
        assertEquals(10, account.getPortfolio().getStock(0).getNumSharesOwned());
        assertEquals(500, account.getPortfolio().getStock(0).getAverageCost());
        assertEquals(APPLE, account.getPortfolio().getStock(1).getStock());
        assertEquals(20, account.getPortfolio().getStock(1).getNumSharesOwned());
        assertEquals(200, account.getPortfolio().getStock(1).getAverageCost());
    }

    @Test
    void testSellSomeStock() {
        account.deposit(10000);
        account.buy(MICROSOFT, 10, 500);
        StockOwned stockOwned = account.getPortfolio().getStock(0);
        account.sell(stockOwned, 5, 510);
        assertEquals(10000 - 10 * 500 + 5 * 510 - Account.DEFAULT_FEE_PER_TRADE * 2, account.getBalance());
        assertEquals(10 - 5, stockOwned.getNumSharesOwned());
        assertEquals(500, stockOwned.getAverageCost());
    }

    @Test
    void testSellAllStock() {
        account.deposit(10000);
        account.buy(MICROSOFT, 10, 500);
        account.buy(APPLE, 20, 200);
        StockOwned stockOwned = account.getPortfolio().getStock(0);
        account.sell(stockOwned, 10, 520);
        assertEquals(10000 - 10 * 500 - 20 * 200 + 10 * 520
                - Account.DEFAULT_FEE_PER_TRADE * 3, account.getBalance());
        assertEquals(null, account.getPortfolio().getStock(MICROSOFT.getSymbol()));
        assertEquals(1,account.getPortfolio().size());
    }

    @Test
    void testSetPassword() {
        account.setPassword("new password");
        assertEquals("new password",account.getPassword());
    }

    @Test
    void testSetFeePerTrade() {
        account.setFeePerTrade(100);
        assertEquals(100,account.getFeePerTrade());
    }

}