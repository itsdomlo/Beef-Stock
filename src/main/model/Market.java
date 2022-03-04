package model;

import java.util.ArrayList;
import java.util.List;

// Represents the stock exchange market
public class Market {

    private List<Stock> market;

    // EFFECTS: initializes an empty market
    public Market() {
        this.market = new ArrayList<>();
    }

    // REQUIRES: stock not in market
    // MODIFIES: this
    // EFFECTS: add stock to market
    public void addStock(Stock stock) {
        this.market.add(stock);
    }

    // EFFECTS: returns stock of given symbol, null otherwise
    public Stock getStock(String symbol) {
        for (Stock stock : this.market) {
            if (stock.getSymbol().equals(symbol.toUpperCase())) {
                return stock;
            }
        }
        return null;
    }

    // REQUIRES: there is a stock at given index
    // EFFECTS: returns stock by index
    public Stock getStock(int index) {
        return this.market.get(index);
    }

    // REQUIRES: stock is in market
    // EFFECTS: returns total value of numShares of stock at market price
    public double stockValueAtMarketPrice(String symbol, int numShares) {
        Stock stock = getStock(symbol);
        return numShares * stock.marketPrice();
    }

    // EFFECTS: returns number of stocks in market
    public int size() {
        return this.market.size();
    }

    // EFFECTS: returns true if market is empty
    public boolean isEmpty() {
        return this.market.isEmpty();
    }

}
