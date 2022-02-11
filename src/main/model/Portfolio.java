package model;

import java.util.ArrayList;
import java.util.List;

// Represents the stock portfolio of a trading account
public class Portfolio {

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
            if (stockOwned.getStock().getSymbol().equals(symbol.toUpperCase())) {
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

    // EFFECTS: returns total market value of portfolio
    public double totalPortfolioMarketValue() {
        double totalValue = 0;
        for (int i = 0; i < this.portfolio.size(); i++) {
            totalValue += this.portfolio.get(i).totalValueAtMarketPrice();
        }
        return totalValue;
    }

    public int size() {
        return this.portfolio.size();
    }

    public boolean isEmpty() {
        return this.portfolio.isEmpty();
    }

}
