package persistence;

//Citation: JsonSerializationDemo

import model.Account;
import model.Portfolio;
import model.StockOwned;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {

    protected void checkAccount(Account expected, Account actual) {
        assertEquals(expected.getUsername(),actual.getUsername());
        assertEquals(expected.getPassword(),actual.getPassword());
        assertEquals(expected.getName(),actual.getName());
        assertEquals(expected.getBalance(),actual.getBalance());
        assertEquals(expected.getFeePerTrade(),actual.getFeePerTrade());
        checkPortfolio(expected.getPortfolio(),actual.getPortfolio());

    }

    private void checkPortfolio(Portfolio expected, Portfolio actual) {
        assertEquals(expected.size(),actual.size());
        for (int i = 0; i < expected.size(); i++) {
            checkStockOwned(expected.getStock(i),actual.getStock(i));
        }
    }

    private void checkStockOwned(StockOwned expected, StockOwned actual) {
        assertEquals(expected.getStockSymbol(),actual.getStockSymbol());
        assertEquals(expected.getNumSharesOwned(),actual.getNumSharesOwned());
        assertEquals(expected.getAverageCost(),actual.getAverageCost());
    }

}
