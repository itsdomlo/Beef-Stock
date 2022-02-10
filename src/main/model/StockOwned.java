package model;

public class StockOwned {

    private Stock stock;
    private int numSharesOwned;
    private double averageCost;

    public StockOwned(Stock stock, int numSharesOwned, double averageCost) {
        this.stock = stock;
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

    // EFFECTS: returns total value of owned stock at market price
    public double totalValueAtMarketPrice() {
        return this.numSharesOwned * stock.marketPrice();
    }

    // EFFECTS: returns profit or loss of owned stock
    public double profitOrLoss() {
        return totalValueAtMarketPrice() - totalValueAtAverageCost();
    }

    // GETTERS
    public Stock getStock() {
        return this.stock;
    }

    public int getNumSharesOwned() {
        return this.numSharesOwned;
    }

    public double getAverageCost() {
        return this.averageCost;
    }

}
