package persistence;

//Citation: JsonSerializationDemo

import model.Account;
import model.Accounts;
import model.Portfolio;
import model.StockOwned;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads accounts from JSON data stored in file
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads accounts from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Accounts read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccounts(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses accounts from JSON object and returns it
    private Accounts parseAccounts(JSONObject jsonObject) {
        Accounts accounts = new Accounts();

        JSONArray jsonArray = jsonObject.getJSONArray("database");
        for (Object json : jsonArray) {
            JSONObject nextAccount = (JSONObject) json;
            addAccount(accounts, nextAccount);
        }
        return accounts;
    }

    private void addAccount(Accounts accounts, JSONObject jsonObject) {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String name = jsonObject.getString("name");
        double balance = jsonObject.getDouble("balance");
        double feePerTrade = jsonObject.getDouble("fee per trade");
        Portfolio portfolio = constructPortfolio(jsonObject.getJSONObject("portfolio"));

        Account account = new Account(username, password, name);
        account.setBalance(balance);
        account.setFeePerTrade(feePerTrade);
        account.setPortfolio(portfolio);

        accounts.addAccount(account);
    }

    private Portfolio constructPortfolio(JSONObject jsonObject) {
        Portfolio portfolio = new Portfolio();

        JSONArray jsonArray = jsonObject.getJSONArray("stock owned");
        for (Object json : jsonArray) {
            JSONObject nextStockOwned = (JSONObject) json;
            addStockOwned(portfolio, nextStockOwned);
        }
        return portfolio;
    }

    private void addStockOwned(Portfolio portfolio, JSONObject jsonObject) {
        String stockSymbol = jsonObject.getString("stock symbol");
        int numSharesOwned = jsonObject.getInt("number of shares owned");
        double averageCost = jsonObject.getDouble("average cost");
        StockOwned stockOwned = new StockOwned(stockSymbol,numSharesOwned,averageCost);

        portfolio.addStockOwned(stockOwned);
    }

}
