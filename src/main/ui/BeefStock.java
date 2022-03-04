package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

//Citation: JsonSerializationDemo

// Beef Stock Trading Application
public class BeefStock {

    private static final int everyMillisecondUpdateStockPrice = 20000;

    private Market market;
    private Accounts accounts;
    private Scanner input;
    private Timer timer;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the trading application
    public BeefStock() {
        runTrading();
    }

    // MODIFIES: this
    // EFFECTS: processes user command on login page
    private void runTrading() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayLoginPage();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("5")) {
                keepGoing = false;
            } else {
                processLoginPageCommand(command);
            }
        }

        timer.cancel();
        System.out.println("See you soon, happy investing!");
    }

    // EFFECTS: displays menu on login page
    private void displayLoginPage() {
        System.out.println("\nWelcome to Beef Stock,");
        System.out.println("your wallet-friendly stock trading platform");
        System.out.println("Please select the following:");
        System.out.println("\t1 -> Create new account");
        System.out.println("\t2 -> Login");
        System.out.println("\t3 -> save accounts to file");
        System.out.println("\t4 -> load accounts from file");
        System.out.println("\t5 -> Exit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command on login page
    private void processLoginPageCommand(String command) {
        switch (command) {
            case "1":
                createAccount();
                break;
            case "2":
                login();
                break;
            case "3":
                saveAccounts();
                break;
            case "4":
                loadAccounts();
                break;
            default:
                System.out.println("Invalid input, please try again.");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new account and add to the accounts database
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

    // EFFECTS: successful login would bring to account's front page
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

    // MODIFIES: this
    // EFFECTS: processes user command on front page
    private void frontPage(Account account) {
        boolean keepGoing = true;
        String command;

        System.out.println("\nWelcome " + account.getName() + ".");
        while (keepGoing) {
            System.out.printf("\nYour buying power is $%.2f.", account.getBalance());
            System.out.printf("\nYour portfolio market value is $%.2f", portfolioTotalMarketValue(account));
            System.out.println("\nYour account total value is "
                    + String.format("$%.2f", account.getBalance() + portfolioTotalMarketValue(account)));
            System.out.println("Your fee per trade is " + String.format("$%.2f", account.getFeePerTrade()));
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

    // EFFECTS: displays menu on front page
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

    // MODIFIES: this
    // EFFECTS: processes user command on front page
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
            default:
                System.out.println("Invalid input, please try again.");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for deposit function
    private void deposit(Account account) {
        System.out.println("\nEnter deposit amount:");
        double amount = input.nextDouble();
        if (amount > 0) {
            account.deposit(amount);
        } else {
            System.out.println("You cannot deposit less than $0, please try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for withdraw function
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

    // EFFECTS: displays overview of all stocks
    private void exploreStockExchange() {
        boolean keepGoing = true;
        String command = null;

        while (keepGoing) {
            System.out.println("\nThe following stocks are available for trade:");
            for (int i = 0; i < market.size(); i++) {
                Stock stock = market.getStock(i);
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

    // EFFECTS: display details of a given stock
    private void processStockDetailsCommand(String symbol) {
        Stock stock = market.getStock(symbol);
        if (stock != null) {
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

    // MODIFIES: this
    // EFFECTS: processes user command for buying stocks
    private void buy(Account account) {
        Stock stock;
        int numSharesToBuy;
        double price;

        System.out.println("\nEnter the stock symbol:");
        String symbol = input.next().toUpperCase();
        stock = market.getStock(symbol);

        if (stock != null) {

            printSpecificBuyInfo(account, stock);

            System.out.println("# of shares to buy (positive integer):");
            numSharesToBuy = (int) input.nextDouble();
            System.out.println("Buying price: (>= ask price)");
            price = input.nextDouble();

            if (numSharesToBuy > 0 && price >= stock.getAskPrice() && price * numSharesToBuy <= account.getBalance()) {
                account.buy(symbol, numSharesToBuy, price);
            } else {
                handleBuyError(account, stock, numSharesToBuy, price);
            }
        } else {
            System.out.println("Invalid stock symbol, please try again");
        }
    }

    // EFFECTS: displays relevant information for buying the given stock
    private void printSpecificBuyInfo(Account account, Stock stock) {
        System.out.println("The current ask price for " + stock.getSymbol() + " is "
                + String.format("$%.2f", stock.getAskPrice()));
    }

    // EFFECTS: displays the corresponding error for buying the given stock
    private void handleBuyError(Account account, Stock stock, int numSharesToBuy, double price) {
        if (price < stock.getAskPrice()) {
            System.out.println("Your price needs to be >= current ask price, please try again.");
        } else if (price * numSharesToBuy > account.getBalance()) {
            System.out.println("Insufficient balance for transaction, please try again.");
        } else {
            System.out.println("# of shares to buy cannot be <=0 or other error, please try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for selling stocks
    private void sell(Account account) {
        StockOwned stockOwned;
        int numSharesToSell;
        double price;

        System.out.println("\nEnter the stock symbol:");
        String symbol = input.next().toUpperCase();
        stockOwned = account.getPortfolio().getStock(symbol);
        Stock stock = market.getStock(symbol);

        if (stockOwned != null) {

            printSpecificSellInfo(stock, stockOwned);

            System.out.println("# of shares to sell:");
            numSharesToSell = (int) input.nextDouble();
            System.out.println("Selling price: (<= bid price)");
            price = input.nextDouble();

            if (numSharesToSell > 0 && numSharesToSell <= stockOwned.getNumSharesOwned()
                    && price > 0 && price <= stock.getBidPrice()) {
                account.sell(stockOwned, numSharesToSell, price);
            } else {
                handleSellError(stock, stockOwned, numSharesToSell, price);
            }
        } else {
            System.out.println("You don't own this stock or invalid stock symbol, please try again");
        }
    }

    // EFFECTS: displays relevant information for selling the given stock
    private void printSpecificSellInfo(Stock stock, StockOwned stockOwned) {
        System.out.println("The current bid price for " + stock.getSymbol() + " is "
                + String.format("$%.2f", stock.getBidPrice()));
        System.out.println("You have " + stockOwned.getNumSharesOwned() + " shares of "
                + stock.getSymbol() + " at average cost of "
                + String.format("$%.2f", stockOwned.getAverageCost()));
    }

    // EFFECTS: displays the corresponding error for selling the given stock
    private void handleSellError(Stock stock, StockOwned stockOwned, int numSharesToSell, double price) {
        if (price > stock.getBidPrice() || price <= 0) {
            System.out.println("Price needs to be <= current bid price and > 0 , please try again.");
        } else {
            System.out.println("Insufficient or invalid shares to sell, please try again.");
        }
    }

    // EFFECTS: displays summary of account's stock portfolio
    private void myPortfolio(Account account) {
        System.out.println("\nHere's your portfolio:");
        for (int i = 0; i < account.getPortfolio().size(); i++) {
            StockOwned stockOwned = account.getPortfolio().getStock(i);
            String symbol = stockOwned.getStockSymbol();
            double averageValue = stockOwned.totalValueAtAverageCost();
            double marketValue = market.stockValueAtMarketPrice(symbol,stockOwned.getNumSharesOwned());
            System.out.println(symbol
                    + " | # of shares owned: " + stockOwned.getNumSharesOwned()
                    + " | Average cost: " + String.format("$%.2f", stockOwned.getAverageCost())
                    + " | Total value at avg. cost: " + String.format("$%.2f", averageValue)
                    + " | Total value at market price: " + String.format("$%.2f", marketValue)
                    + " | Profit / Loss: " + String.format("$%.2f", marketValue - averageValue));
        }
        System.out.println("Total market value of portfolio: "
                + String.format("$%.2f", portfolioTotalMarketValue(account)));
    }

    // EFFECTS: returns total market value of all stocks in the portfolio of given account
    private double portfolioTotalMarketValue(Account account) {
        double portfolioTotalMarketValue = 0;
        for (int i = 0; i < account.getPortfolio().size(); i++) {
            StockOwned stockOwned = account.getPortfolio().getStock(i);
            String symbol = stockOwned.getStockSymbol();
            portfolioTotalMarketValue += market.stockValueAtMarketPrice(symbol,stockOwned.getNumSharesOwned());
        }
        return portfolioTotalMarketValue;
    }

    // MODIFIES: this
    // EFFECTS: initializes the application
    private void init() {
        market = new Market();
        market.addStock(new Stock("AAPL", "Apple Inc.", 176.28, 0.0005,
                16320000000L, 6.01, 1.19, "Technology"));
        market.addStock(new Stock("MSFT", "Microsoft Corporation", 311.21, 0.0003,
                7500000000L, 9.39, 0.89, "Technology"));
        market.addStock(new Stock("GOOG", "Alphabet Inc.", 2829.06, 0.0007,
                315640000L, 112.2, 1.07, "Technology"));
        market.addStock(new Stock("TSLA", "Tesla, Inc.", 932.00, 0.0008,
                1030000000L, 4.9, 2.01, "Capital Goods"));

        accounts = new Accounts();

        input = new Scanner(System.in);
        input.useDelimiter("\n");

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < market.size(); i++) {
                    market.getStock(i).randPrice();
                }
            }
        }, 0, everyMillisecondUpdateStockPrice); //update stock price every given millisecond
    }

    // MODIFIES: this
    // EFFECTS: save existing state of accounts to given file name
    private void saveAccounts() {
        printJsonFilesInData();
        System.out.println("Name your file: (same name as existing file will overwrite it)");
        String fileName = input.next();
        String path = "./data/" + fileName + ".json";
        jsonWriter = new JsonWriter(path);

        try {
            jsonWriter.open();
            jsonWriter.write(accounts);
            jsonWriter.close();
            System.out.println("Accounts are saved to " + path);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + path);
        }
    }

    // MODIFIES: this
    // EFFECTS: load accounts from given file to application
    private void loadAccounts() {
        printJsonFilesInData();
        System.out.println("Please enter file name: (no need .json extension)");
        String fileName = input.next();
        String path = "./data/" + fileName + ".json";
        jsonReader = new JsonReader(path);

        try {
            accounts = jsonReader.read();
            System.out.println("Loaded accounts from " + path);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + path);
        }
    }

    // EFFECTS: displays file name of all json files under ./data
    private void printJsonFilesInData() {
        File folder = new File("./data");
        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".json");
            }
        });

        System.out.println("\nThe following JSON files are present in ./data:");
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
        }

    }

}
