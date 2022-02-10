package model;

import java.util.Random;

public class Stock {

    private String symbol;
    private String name;
    private double bidPrice; // in USD
    private double askPrice; // in USD
    private double bidAskSpread; // as a fraction of the askPrice
    private long totalNumShares;
    private double earningsPerShare; // in USD
    private double beta;
    private String sector;

    // For price fluctuation generator
    private int priceMidForRand;
    private int priceUpForRand;
    private int priceDownForRand;
    private int spreadMidForRand;
    private int spreadUpForRand;
    private int spreadDownForRand;

    // Default price fluctuation parameters
    private static final int DEFAULT_PRICE_MID = 10000;
    private static final int DEFAULT_PRICE_UP = 100;
    private static final int DEFAULT_PRICE_DOWN = 100;
    private static final int DEFAULT_SPREAD_MID = 10000;
    private static final int DEFAULT_SPREAD_UP = 1000;
    private static final int DEFAULT_SPREAD_DOWN = 1000;

    // REQUIRES: symbol is uppercase, askPrice, bitAskSpread, totalNumShares > 0
    //           earningsPerShare >= 0 (0 means making losses)
    // EFFECTS: initializes a stock based on the given information with default price fluctuation parameters
    public Stock(String symbol, String name, double askPrice, double bitAskSpread,
                 long totalNumShares, double earningsPerShare, double beta, String sector) {
        this.symbol = symbol;
        this.name = name;
        this.bidPrice = askPrice * (1 - bitAskSpread);
        this.askPrice = askPrice;
        this.bidAskSpread = bitAskSpread;
        this.totalNumShares = totalNumShares;
        this.earningsPerShare = earningsPerShare;
        this.beta = beta;
        this.sector = sector;

        this.priceMidForRand = DEFAULT_PRICE_MID;
        this.priceUpForRand = DEFAULT_PRICE_UP;
        this.priceDownForRand = DEFAULT_PRICE_DOWN;
        this.spreadMidForRand = DEFAULT_SPREAD_MID;
        this.spreadUpForRand = DEFAULT_SPREAD_UP;
        this.spreadDownForRand = DEFAULT_SPREAD_DOWN;
    }

    // EFFECTS: returns the market price
    public double marketPrice() {
        return (this.bidPrice + this.askPrice) / 2;
    }

    // REQUIRES: earningsPerShare > 0
    // EFFECTS: returns the P/E Ratio
    public double peRatio() {
        return this.marketPrice() / this.earningsPerShare;
    }

    // EFFECTS: returns the market capitalization
    public double marketCap() {
        return this.marketPrice() * this.totalNumShares;
    }

    // MODIFIES: this
    // EFFECTS: randomly generates the next askPrice, bidAskSpread, and bidPrice
    public void randPrice() {
        Random rand = new Random();

        int priceFluctuateRange = (int)((this.priceDownForRand + this.priceUpForRand) * this.beta);
        int priceScaledLowerBound = this.priceMidForRand - (int)(this.priceDownForRand * this.beta);
        int priceFactor = rand.nextInt(priceFluctuateRange) + priceScaledLowerBound;
        this.askPrice = this.askPrice * priceFactor / this.priceMidForRand;

        int spreadFluctuateRange = (int)((this.spreadDownForRand + this.spreadUpForRand) * this.beta);
        int spreadScaledLowerBound = this.spreadMidForRand - (int)(this.spreadDownForRand * this.beta);
        int spreadFactor = rand.nextInt(spreadFluctuateRange) + spreadScaledLowerBound;
        this.bidAskSpread = this.bidAskSpread * spreadFactor / this.spreadMidForRand;

        this.bidPrice = this.askPrice * (1 - this.bidAskSpread);
    }

    // SETTERS:

    // REQUIRES: num > 0
    // MODIFIES: this
    // EFFECTS: sets totalNumShares
    public void setTotalNumShares(long num) {
        this.totalNumShares = num;
    }

    // REQUIRES:  eps >= 0
    // MODIFIES: this
    // EFFECTS: sets earningsPerShare
    public void setEarningsPerShare(double eps) {
        this.earningsPerShare = eps;
    }

    // MODIFIES: this
    // EFFECTS: sets beta
    public void setBeta(double beta) {
        this.beta = beta;
    }

    // REQUIRES: mid > 0
    // MODIFIES: this
    // EFFECTS: sets priceMidForRand
    public void setPriceMidForRand(int mid) {
        this.priceMidForRand = mid;
    }

    // REQUIRES: up > 0
    // MODIFIES: this
    // EFFECTS: sets priceUpForRand
    public void setPriceUpForRand(int up) {
        this.priceUpForRand = up;
    }

    // REQUIRES: down > 0 && down * beta <= priceMidForRand
    // MODIFIES: this
    // EFFECTS: sets priceDownForRand
    public void setPriceDownForRand(int down) {
        this.priceDownForRand = down;
    }

    // REQUIRES: mid > 0
    // MODIFIES: this
    // EFFECTS: sets spreadMidForRand
    public void setSpreadMidForRand(int mid) {
        this.spreadMidForRand = mid;
    }

    // REQUIRES: up > 0
    // MODIFIES: this
    // EFFECTS: sets spreadUpForRand
    public void setSpreadUpForRand(int up) {
        this.spreadUpForRand = up;
    }

    // REQUIRES: down > 0 && down * beta <= spreadMidForRand
    // MODIFIES: this
    // EFFECTS: sets spreadDownForRand
    public void setSpreadDownForRand(int down) {
        this.spreadDownForRand = down;
    }


    // GETTERS
    public String getSymbol() {
        return this.symbol;
    }

    public String getName() {
        return this.name;
    }

    public double getBidPrice() {
        return this.bidPrice;
    }

    public double getAskPrice() {
        return this.askPrice;
    }

    public double getBidAskSpread() {
        return this.bidAskSpread;
    }

    public long getTotalNumShares() {
        return this.totalNumShares;
    }

    public double getEarningsPerShare() {
        return this.earningsPerShare;
    }

    public double getBeta() {
        return this.beta;
    }

    public String getSector() {
        return this.sector;
    }

}
