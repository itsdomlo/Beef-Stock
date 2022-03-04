package persistence;

//Citation: JsonSerializationDemo

import model.Account;
import model.Accounts;
import model.Portfolio;
import model.StockOwned;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    private Account expectedAcc0 = new Account("dom","lo","dominic");
    private Account expectedAcc1 = new Account("athena","li","athena");
    private Account expectedAcc2 = new Account("poor","broke","no money");

    @Test
    void testReaderNonexistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Accounts accounts = reader.read();
            fail("IOException expected but none caught");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyAccounts() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAccounts.json");
        try {
            Accounts accounts = reader.read();
            assertEquals(0,accounts.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralAccounts() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAccounts.json");
        try {
            Accounts accounts = reader.read();
            assertEquals(3,accounts.size());

            Account acc0 = accounts.getAccount(0);
            Account acc1 = accounts.getAccount(1);
            Account acc2 = accounts.getAccount(2);

            checkAccount(setupExpectedAcc0(),acc0);
            checkAccount(setupExpectedAcc1(),acc1);
            checkAccount(setupExpectedAcc2(),acc2);

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    private Account setupExpectedAcc0() {
        expectedAcc0.setBalance(44995);
        Portfolio portfolio = new Portfolio();
        portfolio.addStockOwned(new StockOwned("TSLA",5,1000));
        expectedAcc0.setPortfolio(portfolio);
        return expectedAcc0;
    }

    private Account setupExpectedAcc1() {
        expectedAcc1.setBalance(76990);
        Portfolio portfolio = new Portfolio();
        portfolio.addStockOwned(new StockOwned("AAPL",10,200));
        portfolio.addStockOwned((new StockOwned("MSFT",2,500)));
        expectedAcc1.setPortfolio(portfolio);
        return expectedAcc1;
    }

    private Account setupExpectedAcc2() {
        expectedAcc2.setBalance(0);
        Portfolio portfolio = new Portfolio();
        expectedAcc2.setPortfolio(portfolio);
        return expectedAcc2;
    }

}
