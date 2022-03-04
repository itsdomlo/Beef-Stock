package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    private static final String USERNAME = "dom";
    private static final String PASSWORD = "123";
    private static final String NAME = "Dominic";

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
        account.buy("MSFT", 10, 500);
        assertEquals(10000 - 10 * 500 - Account.DEFAULT_FEE_PER_TRADE, account.getBalance());
        assertEquals(1, account.getPortfolio().size());
        assertEquals("MSFT", account.getPortfolio().getStock(0).getStockSymbol());
        assertEquals(10, account.getPortfolio().getStock(0).getNumSharesOwned());
        assertEquals(500, account.getPortfolio().getStock(0).getAverageCost());
    }


    @Test
    void testBuyStockAlreadyOwned() {
        account.deposit(20000);
        account.buy("MSFT", 10, 500);
        account.buy("MSFT", 10, 600);
        assertEquals(10 + 10, account.getPortfolio().getStock(0).getNumSharesOwned());
        assertEquals((10 * 500 + 10 * 600) / (10 + 10), account.getPortfolio().getStock(0).getAverageCost());
        assertEquals(1, account.getPortfolio().size());
    }

    @Test
    void testBuyTwoStocksNotYetOwned() {
        account.deposit(10000);
        account.buy("MSFT", 10, 500);
        account.buy("AAPL", 20, 200);
        assertEquals(10000 - 10 * 500 - 20 * 200 - Account.DEFAULT_FEE_PER_TRADE * 2, account.getBalance());
        assertEquals(2, account.getPortfolio().size());
        assertEquals("MSFT", account.getPortfolio().getStock(0).getStockSymbol());
        assertEquals(10, account.getPortfolio().getStock(0).getNumSharesOwned());
        assertEquals(500, account.getPortfolio().getStock(0).getAverageCost());
        assertEquals("AAPL", account.getPortfolio().getStock(1).getStockSymbol());
        assertEquals(20, account.getPortfolio().getStock(1).getNumSharesOwned());
        assertEquals(200, account.getPortfolio().getStock(1).getAverageCost());
    }

    @Test
    void testSellSomeStock() {
        account.deposit(10000);
        account.buy("MSFT", 10, 500);
        StockOwned stockOwned = account.getPortfolio().getStock(0);
        account.sell(stockOwned, 5, 510);
        assertEquals(10000 - 10 * 500 + 5 * 510 - Account.DEFAULT_FEE_PER_TRADE * 2, account.getBalance());
        assertEquals(10 - 5, stockOwned.getNumSharesOwned());
        assertEquals(500, stockOwned.getAverageCost());
    }

    @Test
    void testSellAllStock() {
        account.deposit(10000);
        account.buy("MSFT", 10, 500);
        account.buy("AAPL", 20, 200);
        assertEquals(2,account.getPortfolio().size());
        StockOwned stockOwned = account.getPortfolio().getStock(0);
        account.sell(stockOwned, 10, 520);
        assertEquals(10000 - 10 * 500 - 20 * 200 + 10 * 520
                - Account.DEFAULT_FEE_PER_TRADE * 3, account.getBalance());
        assertEquals(null, account.getPortfolio().getStock("MSFT"));
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

    @Test
    void testSetBalance() {
        account.setBalance(1234);
        assertEquals(1234,account.getBalance());
    }

    @Test
    void testSetPortfolio() {
        Portfolio portfolio = new Portfolio();
        StockOwned stockOwned = new StockOwned("TSLA",10,1000);
        portfolio.addStockOwned(stockOwned);
        account.setPortfolio(portfolio);
        assertEquals(portfolio,account.getPortfolio());
    }

}