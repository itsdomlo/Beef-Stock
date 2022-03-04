package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents the stock portfolio of a trading account
public class Portfolio implements Writable {

    private List<StockOwned> portfolio;

    // EFFECTS: initializes an empty portfolio
    public Portfolio() {
        this.portfolio = new ArrayList<>();
    }

    // REQUIRES: stockOwned not in portfolio, numSharesToBuy, price > 0
    // MODIFIES: this
    // EFFECTS: adds a new StockOwned to portfolio
    public void addStockOwned(StockOwned stockOwned) {
        this.portfolio.add(stockOwned);
    }

    // REQUIRES: stockOwned is in portfolio
    // MODIFIES: this
    // EFFECTS: removes stock from portfolio
    public void removeStockOwned(StockOwned stockOwned) {
        this.portfolio.remove(stockOwned);
    }

    // EFFECTS: returns StockOwned of given symbol, null otherwise
    public StockOwned getStock(String symbol) {
        for (StockOwned stockOwned : this.portfolio) {
            if (stockOwned.getStockSymbol().equals(symbol)) {
                return stockOwned;
            }
        }
        return null;
    }

    // REQUIRES: there is a StockOwned at index
    // EFFECTS: returns StockOwned at index
    public StockOwned getStock(int index) {
        return this.portfolio.get(index);
    }

    // EFFECTS: returns number of stocks owned in the portfolio
    public int size() {
        return this.portfolio.size();
    }

    // EFFECTS: returns true is portfolio is empty, false otherwise
    public boolean isEmpty() {
        return this.portfolio.isEmpty();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("stock owned",stockOwnedToJson());
        return json;
    }

    // EFFECTS: returns all stocks owned in this portfolio as a JSON array
    private JSONArray stockOwnedToJson() {
        JSONArray jsonArray = new JSONArray();

        for (StockOwned stockOwned : portfolio) {
            jsonArray.put(stockOwned.toJson());
        }

        return jsonArray;
    }

}
