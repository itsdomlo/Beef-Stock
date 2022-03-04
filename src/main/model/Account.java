package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Represents a trading account for a user
public class Account implements Writable {
    private String username;
    private String password;
    private String name;
    private double balance; //USD
    private double feePerTrade; //USD
    private Portfolio portfolio;

    protected static final double DEFAULT_FEE_PER_TRADE = 5; //USD

    // REQUIRES: username, password and name has non-zero length
    // EFFECTS: creates a trading account with given username, password and name.
    //          Account balance is set to $0, feePerTrade set as DEFAULT_FEE_PER_TRADE
    //          and an empty portfolio.
    public Account(String username, String password, String name) {
        this.username = username.toLowerCase();
        this.password = password;
        this.name = name;
        this.balance = 0;
        this.feePerTrade = DEFAULT_FEE_PER_TRADE;
        portfolio = new Portfolio();
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: add amount to account balance
    public void deposit(double amount) {
        this.balance += amount;
    }

    // REQUIRES: amount > 0 and amount <= getBalance()
    // MODIFIES: this
    // EFFECTS: deduct amount from account balance
    public void withdraw(double amount) {
        this.balance -= amount;
    }

    // REQUIRES: balance >= total cost of shares to buy, price >= stock.askPrice (for now)
    // MODIFIES: this
    // EFFECTS: Purchase numSharesToBuy stocks of given symbol at price.
    //          Deduct cost and fee from balance.
    //          If own stock already, add shares to existing StockOwned in portfolio.
    //          If not own stock, add new StockOwned to portfolio.
    public void buy(String symbol, int numSharesToBuy, double price) {
        this.balance = this.balance - numSharesToBuy * price - this.feePerTrade;
        StockOwned s = this.portfolio.getStock(symbol);
        if (s != null) {
            s.buyStockOwned(numSharesToBuy,price);
        } else {
            StockOwned stockOwned = new StockOwned(symbol,numSharesToBuy,price);
            this.portfolio.addStockOwned(stockOwned);
        }
    }

    // REQUIRES: Owns this stock, numSharesToSell <= shares owned, numSharesToSell, price <= stock.bidPrice (for now)
    // MODIFIES: this
    // EFFECTS: Sell numSharesToSell stocks of given symbol at price.
    //          Add proceeds net fee to balance.
    //          If numSharesToSell < shares owned, deduct shares from StockOwned in portfolio.
    //          If numSharesToSell = shares owned, remove StockOwned from portfolio.
    public void sell(StockOwned stockOwned, int numSharesToSell, double price) {
        this.balance = this.balance + numSharesToSell * price - this.feePerTrade;
        if (stockOwned.getNumSharesOwned() > numSharesToSell) {
            stockOwned.sellStockOwned(numSharesToSell);
        } else {
            this.portfolio.removeStockOwned(stockOwned);
        }
    }

    // SETTERS

    // REQUIRES: newPassword non-zero string length
    // MODIFIES: this
    // EFFECTS: changes password
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    // REQUIRES: newFeePerTrade >= 0
    // MODIFIES: this
    // EFFECTS: changes fee per trade
    public void setFeePerTrade(double newFeePerTrade) {
        this.feePerTrade = newFeePerTrade;
    }

    //TODO
    public void setBalance(double balance) {
        this.balance = balance;
    }

    //TODO
    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }


    // GETTERS
    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public double getBalance() {
        return this.balance;
    }

    public double getFeePerTrade() {
        return this.feePerTrade;
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("username",username);
        json.put("password",password);
        json.put("name",name);
        json.put("balance",balance);
        json.put("fee per trade",feePerTrade);
        json.put("portfolio",getPortfolio().toJson());
        return json;
    }

}
