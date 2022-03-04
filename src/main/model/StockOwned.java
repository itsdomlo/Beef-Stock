package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a stock owned in the portfolio
public class StockOwned implements Writable {

    // private Stock stock;
    private String stockSymbol;
    private int numSharesOwned;
    private double averageCost;

    // EFFECTS: initialize a newly owned stock
    public StockOwned(String stockSymbol, int numSharesOwned, double averageCost) {
        this.stockSymbol = stockSymbol;
        this.numSharesOwned = numSharesOwned;
        this.averageCost = averageCost;
    }

    // REQUIRES: numSharesToBuy, price > 0
    // MODIFIES: this
    // EFFECTS: increase numSharesOwned and update average cost
    public void buyStockOwned(int numSharesToBuy, double price) {
        this.averageCost = (this.numSharesOwned * this.averageCost + numSharesToBuy * price)
                / (this.numSharesOwned + numSharesToBuy);
        this.numSharesOwned += numSharesToBuy;
    }

    // REQUIRES: numSharesToSell > 0
    // MODIFIES: this
    // EFFECTS: decrease numSharesOwned
    public void sellStockOwned(int numSharesToSell) {
        this.numSharesOwned -= numSharesToSell;
    }

    // EFFECTS: returns total value of owned stock at average cost
    public double totalValueAtAverageCost() {
        return this.numSharesOwned * this.averageCost;
    }

    // GETTERS
    public String getStockSymbol() {
        return this.stockSymbol;
    }

    public int getNumSharesOwned() {
        return this.numSharesOwned;
    }

    public double getAverageCost() {
        return this.averageCost;
    }

    // EFFECTS: returns stock owned as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("stock symbol",stockSymbol);
        json.put("number of shares owned",numSharesOwned);
        json.put("average cost",averageCost);
        return json;
    }
}
