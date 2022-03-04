package persistence;

//Citation: JsonSerializationDemo

import model.Account;
import model.Accounts;
import model.Portfolio;
import model.StockOwned;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {

    private Account expectedAcc0 = new Account("harry","potter","the chosen one");
    private Account expectedAcc1 = new Account("eren","yeager","tatakae");
    private Account expectedAcc2 = new Account("putin","idiot","war criminal");

    @Test
    void testWriterInvalidFile() {
        JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
        try {
            writer.open();
            fail("IOException was expected but none caught");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyAccounts() {
        Accounts accounts = new Accounts();
        JsonWriter writer = new JsonWriter("./data/testWriterEmptyAccounts.json");

        try {
            writer.open();
            writer.write(accounts);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyAccounts.json");
            accounts = reader.read();
            assertEquals(0,accounts.size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralAccounts() {
        Accounts accounts = new Accounts();
        accounts.addAccount(setupExpectedAcc0());
        accounts.addAccount(setupExpectedAcc1());
        accounts.addAccount(setupExpectedAcc2());
        JsonWriter writer = new JsonWriter("./data/testWriterGeneralAccounts.json");

        try {
            writer.open();
            writer.write(accounts);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralAccounts.json");
            accounts = reader.read();

            Account acc0 = accounts.getAccount(0);
            Account acc1 = accounts.getAccount(1);
            Account acc2 = accounts.getAccount(2);

            checkAccount(setupExpectedAcc0(),acc0);
            checkAccount(setupExpectedAcc1(),acc1);
            checkAccount(setupExpectedAcc2(),acc2);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }

    }

    private Account setupExpectedAcc0() {
        expectedAcc0.setBalance(10000);
        Portfolio portfolio = new Portfolio();
        portfolio.addStockOwned(new StockOwned("GOOG",10,3000));
        expectedAcc0.setPortfolio(portfolio);
        return expectedAcc0;
    }

    private Account setupExpectedAcc1() {
        expectedAcc1.setBalance(9000);
        Portfolio portfolio = new Portfolio();
        portfolio.addStockOwned(new StockOwned("TSLA",5,300));
        portfolio.addStockOwned((new StockOwned("AAPL",10,400)));
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
