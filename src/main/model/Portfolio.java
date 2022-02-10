package model;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {

    private List<StockOwned> portfolio;

    public Portfolio() {
        this.portfolio = new ArrayList<>();
    }

    // REQUIRES: stock not in portfolio, stock in market, numSharesToBuy, price > 0
    // MODIFIES: this
    // EFFECTS: adds newly purchased stock to portfolio
    public void addStock(Stock stock,int numSharesToBuy, double price) {
        this.portfolio.add(new StockOwned(stock,numSharesToBuy,price));
    }

    // MODIFIES: this
    // EFFECTS: removes stock from portfolio
    public void removeStock(StockOwned stockOwned) {
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

}
