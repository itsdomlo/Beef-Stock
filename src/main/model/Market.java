package model;

import java.util.ArrayList;
import java.util.List;

public class Market {

    private List<Stock> market;
    private double marketEventFactor; // factor to impact all the stocks' price in market

    // EFFECTS: initializes an empty market with market event factor = 1
    public Market() {
        this.market = new ArrayList<>();
        this.marketEventFactor = 1;
    }

    // REQUIRES: stock not in market
    // MODIFIES: this
    // EFFECTS: add stock to market
    public void addStock(Stock stock) {
        this.market.add(stock);
    }

    // REQUIRES: stock in market
    // EFFECTS: returns stock of given symbol, null otherwise
    public Stock getStock(String symbol) {
        for (Stock stock : this.market) {
            if (stock.getSymbol().equals(symbol.toUpperCase())) {
                return stock;
            }
        }
        return null;
    }

    // EFFECTS: returns stock by index
    public Stock getStock(int index) {
        return this.market.get(index);
    }

    // REQUIRES: newFactor > 0
    // MODIFIES: this
    // EFFECTS: sets marketEventFactor
    public void setMarketEventFactor(double newFactor) {
        this.marketEventFactor = newFactor;
    }

    public int size() {
        return this.market.size();
    }

}
