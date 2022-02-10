package ui;

import model.*;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class TradingApp {

    private Market market;
    private Accounts accounts;
    private Scanner input;
    private Timer timer;

    // EFFECTS: runs the trading application
    public TradingApp() {
        runTrading();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runTrading() {
        boolean keepGoing = true;
        String command = null;

        init();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < market.size(); i++) {
                    market.getStock(i).randPrice();
                }
            }
        }, 0, 10000); //updates every given millisecond


        while (keepGoing) {
            displayLoginPage();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("e")) {
                keepGoing = false;
            } else {
                processLoginPageCommand(command);
            }
        }

        timer.cancel();
        System.out.println("See you soon, happy investing!");
    }

    private void displayLoginPage() {
        System.out.println("\nWelcome to Beef Stock,");
        System.out.println("your wallet-friendly stock trading platform");
        System.out.println("Please select the following:");
        System.out.println("\tn -> Create new account");
        System.out.println("\tl -> Login");
        System.out.println("\te -> Exit");
    }

    private void processLoginPageCommand(String command) {
        if (command.equals("n")) {
            createAccount();
        } else if (command.equals("l")) {
            login();
        } else {
            System.out.println("Invalid input, please try again.");
        }
    }

    private void createAccount() {
        boolean isUsernameUsed = true;
        String username = null;
        while (isUsernameUsed) {
            System.out.println("\nWhat would you like your username (not case-sensitive) to be?");
            username = input.next();
            if (accounts.isUsernameUsedAlready(username)) {
                System.out.println("Username is used already, please try again.");
            } else {
                isUsernameUsed = false;
            }
        }
        System.out.println("What would you like your password (case-sensitive) to be?");
        String password = input.next();
        System.out.println("How would your like us to call you?");
        String name = input.next();

        Account newAccount = new Account(username, password, name);
        accounts.addAccount(newAccount);

        System.out.println("Thank you " + name + ", please note your username and password,");
        System.out.println("you will need them to login.");
    }

    private void login() {
        System.out.println("\nPlease enter your username:");
        String username = input.next();
        System.out.println("Please enter your password:");
        String password = input.next();
        Account account = accounts.getAccount(username, password);
        if (account != null) {
            frontPage(account);
        } else {
            System.out.println("Incorrect username/password or no such account,");
            System.out.println("please try again.");
        }
    }

    private void frontPage(Account account) {
        boolean keepGoing = true;
        String command = null;

        System.out.println("\nWelcome " + account.getName() + ".");
        while (keepGoing) {
            System.out.printf("\nYour buying power is $%.2f.", account.getBalance());
            System.out.printf("\nYour portfolio market value is $%.2f",
                    account.getPortfolio().totalPortfolioMarketValue());
            System.out.println("\nYour account total value is "
                    + String.format("$%.2f", account.getBalance()
                    + account.getPortfolio().totalPortfolioMarketValue()));
            System.out.println("Your fee per trade is " + String.format("$%.2f",account.getFeePerTrade()));
            displayFrontPage();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("l")) {
                System.out.println("See you soon " + account.getName());
                keepGoing = false;
            } else {
                processFrontPageCommand(command, account);
            }
        }
    }

    private void displayFrontPage() {
        System.out.println("\nPlease select the following:");
        System.out.println("\td -> Deposit money");
        System.out.println("\tw -> Withdraw money");
        System.out.println("\te -> Explore the stock exchange");
        System.out.println("\tb -> Buy stock");
        System.out.println("\ts -> Sell stock");
        System.out.println("\tp -> My portfolio");
        System.out.println("\tl -> Logout");
    }

    private void processFrontPageCommand(String command, Account account) {
        switch (command.toLowerCase()) {
            case "d":
                deposit(account);
                break;
            case "w":
                withdraw(account);
                break;
            case "e":
                exploreStockExchange();
                break;
            case "b":
                buy(account);
                break;
            case "s":
                sell(account);
                break;
            case "p":
                myPortfolio(account);
                break;
        }
    }

    private void deposit(Account account) {
        System.out.println("\nEnter deposit amount:");
        double amount = input.nextDouble();
        if (amount > 0) {
            account.deposit(amount);
        } else {
            System.out.println("You cannot deposit less than $0, please try again.");
        }
    }

    private void withdraw(Account account) {
        System.out.println("\nEnter withdrawal amount:");
        double amount = input.nextDouble();
        if (amount < 0) {
            System.out.println("You cannot withdraw less than $0, please try again.");
        } else if (amount > account.getBalance()) {
            System.out.println("Your account balance is lower than the withdrawal amount, please try again.");
        } else {
            account.withdraw(amount);
        }
    }

    private void exploreStockExchange() {
        boolean keepGoing = true;
        String command = null;

        while (keepGoing) {
            System.out.println("\nThe following stocks are in the stock exchange:");
            for (int i = 0; i < market.size(); i++) {
                Stock stock = market.getStock(i);
                //stock.randPrice();
                System.out.println(stock.getSymbol() + " | " + stock.getName()
                        + " | Bid Price: $" + String.format("%.2f", stock.getBidPrice())
                        + " | Ask Price: $" + String.format("%.2f", stock.getAskPrice()));
            }
            System.out.println("\nPlease select the following:");
            System.out.println("\tEnter stock symbol for more information");
            System.out.println("\tr -> Return");
            String symbol = input.next();
            if (symbol.equals("r")) {
                keepGoing = false;
            } else {
                processStockDetailsCommand(symbol);
            }
        }
    }

    private void processStockDetailsCommand(String symbol) {
        Stock stock = market.getStock(symbol);
        if (stock != null) {
            //stock.randPrice();
            System.out.println("\nStock symbol: " + stock.getSymbol());
            System.out.println("Company: " + stock.getName());
            System.out.println("Sector: " + stock.getSector());
            System.out.println("Bid Price: " + String.format("$%.2f", stock.getBidPrice()));
            System.out.println("Ask Price: " + String.format("$%.2f", stock.getAskPrice()));
            System.out.println("Market Cap.: " + String.format("$%.0f", stock.marketCap() / 1e6) + "M");
            System.out.println("P/E Ratio: " + String.format("%.2f", stock.peRatio()));
            System.out.println("Beta: " + stock.getBeta());
            System.out.println("Earnings per share: " + stock.getEarningsPerShare());
            System.out.println("Total # of outstanding shares: " + stock.getTotalNumShares() / 1e6 + "M");
        } else {
            System.out.println("Invalid stock symbol, please try again.");
        }
    }

    private void buy(Account account) {
        Stock stock = null;
        int numSharesToBuy = 0;
        double price = 0;

        System.out.println("\nEnter the stock symbol:");
        String symbol = input.next();
        stock = market.getStock(symbol);
        if (stock != null) {
            //stock.randPrice(); // for now
            System.out.println("The current ask price for " + stock.getSymbol() + " is "
                    + String.format("$%.2f", stock.getAskPrice()));
            System.out.println("# of shares to buy:");
            numSharesToBuy = input.nextInt();
            if (numSharesToBuy > 0) {
                System.out.println("Buying price: (>= ask price)");
                price = input.nextDouble();
                if (price < stock.getAskPrice()) {
                    System.out.println("Your price needs to be >= current ask price, please try again.");
                } else if (price * numSharesToBuy > account.getBalance()) {
                    System.out.println("Insufficient balance for transaction, please try again.");
                } else {
                    account.buy(stock, numSharesToBuy, price);
                }
            } else {
                System.out.println("# of shares to buy cannot be <0, please try again.");
            }
        } else {
            System.out.println("Invalid stock symbol, please try again");
        }
    }

    private void sell(Account account) {
        StockOwned stockOwned;
        int numSharesToSell;
        double price = 0;

        System.out.println("\nEnter the stock symbol:");
        String symbol = input.next();
        stockOwned = account.getPortfolio().getStock(symbol);
        Stock stock = market.getStock(symbol);
        if (stockOwned != null) {
            //stock.randPrice(); // for now
            System.out.println("The current bid price for " + stock.getSymbol() + " is "
                    + String.format("$%.2f", stock.getBidPrice()));
            System.out.println("You have " + stockOwned.getNumSharesOwned() + " shares of "
                    + stock.getSymbol() + " at average cost of "
                    + String.format("$%.2f", stockOwned.getAverageCost()));
            System.out.println("# of shares to sell:");
            numSharesToSell = input.nextInt();
            if (numSharesToSell > 0 && numSharesToSell <= stockOwned.getNumSharesOwned()) {
                System.out.println("Selling price: (<= bid price)");
                price = input.nextDouble();
                if (price > 0 && price <= stock.getBidPrice()) {
                    account.sell(stockOwned, numSharesToSell, price);
                } else {
                    System.out.println("Price needs to be <= current bid price and > 0 , please try again.");
                }
            } else {
                System.out.println("Insufficient or invalid shares to sell, please try again.");
            }
        } else {
            System.out.println("You don't own this stock or invalid stock symbol, please try again");
        }
    }

    private void myPortfolio(Account account) {
        System.out.println("\nHere's your portfolio:");
        for (int i = 0; i < account.getPortfolio().size(); i++) {
            StockOwned stockOwned = account.getPortfolio().getStock(i);
            System.out.println(stockOwned.getStock().getSymbol()
                    + " | # of shares owned: " + stockOwned.getNumSharesOwned()
                    + " | Average cost: " + String.format("$%.2f", stockOwned.getAverageCost())
                    + " | Total value at avg. cost: " + String.format("$%.2f", stockOwned.totalValueAtAverageCost())
                    + " | Total value at market price: " + String.format("$%.2f", stockOwned.totalValueAtMarketPrice())
                    + " | Profit / Loss: " + String.format("$%.2f", stockOwned.profitOrLoss()));
        }
        System.out.println("Total market value of portfolio: "
                + String.format("$%.2f", account.getPortfolio().totalPortfolioMarketValue()));
    }

    // MODIFIES: this
    // EFFECTS: initializes the market and accounts
    private void init() {
        Stock apple = new Stock("AAPL", "Apple Inc.", 176.28, 0.0005,
                16320000000L, 6.01, 1.19, "Technology");
        Stock microsoft = new Stock("MSFT", "Microsoft Corporation", 311.21, 0.0003,
                7500000000L, 9.39, 0.89, "Technology");
        Stock google = new Stock("GOOG", "Alphabet Inc.", 2829.06, 0.0007,
                315640000L, 112.2, 1.07, "Technology");
        Stock tesla = new Stock("TSLA", "Tesla, Inc.", 932.00, 0.0008,
                1030000000L, 4.9, 2.01, "Capital Goods");
        Stock visa = new Stock("V", "Visa Inc.", 230.87, 0.0004,
                1660000000L, 6.04, 0.92, "Financial Services");
        market = new Market();
        market.addStock(apple);
        market.addStock(microsoft);
        market.addStock(google);
        market.addStock(tesla);
        market.addStock(visa);

        Account presetAccount = new Account("dom123", "123", "Dominic");
        accounts = new Accounts();
        accounts.addAccount(presetAccount);

        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }


}
