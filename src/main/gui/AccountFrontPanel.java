package gui;

import model.Account;
import model.Market;
import model.StockOwned;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the logged in account panel
public class AccountFrontPanel extends JPanel implements ActionListener {

    protected static final String ACCOUNT_FRONT = "accountFront";
    protected static final String DEPOSIT = "deposit";
    protected static final String WITHDRAW = "withdraw";
    protected static final String EXPLORE = "explore";
    protected static final String BUY = "buy";
    protected static final String SELL = "sell";
    protected static final String PORTFOLIO = "portfolio";
    protected static final String LOGOUT = "logout";

    private JPanel accountFrontPanel;
    private AccountDepositPanel accountDepositPanel;
    private AccountWithdrawPanel accountWithdrawPanel;
    private AccountExplorePanel accountExplorePanel;
    private AccountBuyPanel accountBuyPanel;
    private AccountSellPanel accountSellPanel;
    private AccountPortfolioPanel accountPortfolioPanel;

    private JLabel buyingPowerLabel = new JLabel();
    private JLabel portfolioValueLabel = new JLabel();
    private JLabel accountValueLabel = new JLabel();

    private FrontLoginPanel loggedOutPanel;
    private Account account;
    private Timer timer;

    // EFFECTS: constructs as a card panel which contains all related panels
    public AccountFrontPanel(FrontLoginPanel loggedOutPanel, Account account) {
        super(new CardLayout());
        this.loggedOutPanel = loggedOutPanel;
        this.account = account;

        setUpAccountFrontPanel();
        this.add(accountFrontPanel, ACCOUNT_FRONT);

        accountDepositPanel = new AccountDepositPanel(this);
        this.add(accountDepositPanel, DEPOSIT);

        accountWithdrawPanel = new AccountWithdrawPanel(this);
        this.add(accountWithdrawPanel, WITHDRAW);

        accountExplorePanel = new AccountExplorePanel(this);
        this.add(accountExplorePanel, EXPLORE);

        accountBuyPanel = new AccountBuyPanel(this);
        this.add(accountBuyPanel, BUY);

        accountSellPanel = new AccountSellPanel(this);
        this.add(accountSellPanel, SELL);

        accountPortfolioPanel = new AccountPortfolioPanel(this);
        this.add(accountPortfolioPanel, PORTFOLIO);
    }

    // MODIFIES: this
    // EFFECTS: sets up the account front panel
    private void setUpAccountFrontPanel() {

        accountFrontPanel = new JPanel();

        BoxLayout bl = new BoxLayout(accountFrontPanel, BoxLayout.Y_AXIS);
        accountFrontPanel.setLayout(bl);
        Border border = accountFrontPanel.getBorder();
        accountFrontPanel.setBorder(new CompoundBorder(border, FrontGUI.MARGIN));

        alignLabels();
        loadLabels();
        regularRefreshLabels();
        accountFrontPanel.add(new JLabel("Welcome " + account.getName() + "."));
        accountFrontPanel.add(buyingPowerLabel);
        accountFrontPanel.add(portfolioValueLabel);
        accountFrontPanel.add(accountValueLabel);
        accountFrontPanel.add(new JLabel(String.format("Your fee per trade is $%,.2f", account.getFeePerTrade())));

        addButton("Deposit money", DEPOSIT);
        addButton("Withdraw money", WITHDRAW);
        addButton("Explore the stock exchange", EXPLORE);
        addButton("Buy stock", BUY);
        addButton("Sell stock", SELL);
        addButton("My portfolio", PORTFOLIO);
        addButton("Logout", LOGOUT);
    }

    // MODIFIES: this
    // EFFECTS: aligns labels on account front panel
    private void alignLabels() {
        buyingPowerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        portfolioValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // MODIFIES: this
    // EFFECTS: regularly refresh relevant labels on account front panel
    private void regularRefreshLabels() {
        timer = new Timer(FrontGUI.everyMillisecondUpdateStockPrice, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadLabels();
            }
        });
        timer.start();
    }

    // MODIFIES: this
    // EFFECTS: loads information to the labels on account front panel
    private void loadLabels() {
        buyingPowerLabel.setText(String.format("Your buying power is $%,.2f.", account.getBalance()));
        portfolioValueLabel.setText(String.format("Your portfolio market value is $%,.2f",
                portfolioTotalMarketValue()));
        accountValueLabel.setText(String.format("Your account total value is $%,.2f",
                account.getBalance() + portfolioTotalMarketValue()));
    }

    // EFFECTS: returns total market value of all stocks in the portfolio of given account
    private double portfolioTotalMarketValue() {
        Market market = loggedOutPanel.getFrontGUI().getMarket();
        double portfolioTotalMarketValue = 0;
        for (int i = 0; i < account.getPortfolio().size(); i++) {
            StockOwned stockOwned = account.getPortfolio().getStock(i);
            String symbol = stockOwned.getStockSymbol();
            portfolioTotalMarketValue += market.stockValueAtMarketPrice(symbol, stockOwned.getNumSharesOwned());
        }
        return portfolioTotalMarketValue;
    }

    // MODIFIES: this
    // EFFECTS: adds a button with action listener to account front panel
    private void addButton(String text, String command) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountFrontPanel.add(button);
    }

    // EFFECTS: returns account
    public Account getAccount() {
        return account;
    }

    // EFFECTS: returns the logged out panel
    public FrontLoginPanel getLoginGUI() {
        return loggedOutPanel;
    }

    // MODIFIES: this
    // EFFECTS: handles the actions performed on this panel
    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout clForLogout = (CardLayout) loggedOutPanel.getLayout();
        CardLayout clThis = (CardLayout) this.getLayout();
        String command = e.getActionCommand();
        handleCommand(clForLogout, clThis, command);
    }

    // MODIFIES: this
    // EFFECTS: details of handling the actions performed
    private void handleCommand(CardLayout clForLogout, CardLayout clThis, String command) {
        switch (command) {
            case DEPOSIT:
                clThis.show(this, DEPOSIT);
                break;
            case WITHDRAW:
                clThis.show(this, WITHDRAW);
                break;
            case EXPLORE:
                handleExplore(clThis);
                break;
            case BUY:
                clThis.show(this, BUY);
                break;
            case SELL:
                handleSell(clThis);
                break;
            case PORTFOLIO:
                handlePortfolio(clThis);
                break;
            case LOGOUT:
                handleLogout(clForLogout);
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: handle the logout action
    private void handleLogout(CardLayout clForLogout) {
        timer.stop();
        clForLogout.show(loggedOutPanel, FrontLoginPanel.LOGIN_PANEL);
    }

    // MODIFIES: this
    // EFFECTS: handle the portfolio action
    private void handlePortfolio(CardLayout clThis) {
        accountPortfolioPanel.setUpTableModel();
        accountPortfolioPanel.regularRefreshData();
        clThis.show(this, PORTFOLIO);
    }

    // MODIFIES: this
    // EFFECTS: handle the sell action
    private void handleSell(CardLayout clThis) {
        accountSellPanel.updateComboBox();
        clThis.show(this, SELL);
    }

    // MODIFIES: this
    // EFFECTS: handle the explore action
    private void handleExplore(CardLayout clThis) {
        accountExplorePanel.regularRefreshData();
        clThis.show(this, EXPLORE);
    }
}
