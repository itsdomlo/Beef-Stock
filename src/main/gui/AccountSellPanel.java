package gui;

import model.*;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Represents the sell panel
public class AccountSellPanel extends AbstractPanel {

    protected static final String COMBO = "combo";
    protected static final String SELL = "sell";

    private AccountFrontPanel accountFrontPanel;
    private JComboBox comboBox;
    private JLabel comboBoxMessage;
    private JLabel averageCostMessage;
    private JSpinner spinner;
    private JFormattedTextField priceField;
    private JLabel sellMessage;
    private Timer timer;

    // EFFECTS: constructs and sets up the buy panel
    public AccountSellPanel(AccountFrontPanel accountFrontPanel) {
        super();
        this.accountFrontPanel = accountFrontPanel;
        addStaticLabel("Choose a stock to sell:");
        addComboBox();
        comboBoxMessage = new JLabel(" ");
        this.add(comboBoxMessage);
        averageCostMessage = new JLabel(" ");
        this.add(averageCostMessage);
        addStaticLabel(" ");
        addStaticLabel("Number to sell:");
        addSpinner();
        addStaticLabel("Selling price per stock:");
        addPriceField();
        sellMessage = new JLabel(" ");
        this.add(sellMessage);
        addStaticLabel(" ");
        addButton("Sell", SELL);
        addButton("Back", BACK);
    }

    // MODIFIES: this
    // EFFECTS: adds the combo box with stocks available to sell to this
    private void addComboBox() {
        Portfolio portfolio = accountFrontPanel.getAccount().getPortfolio();

        List<String> stockList = new ArrayList<>();
        stockList.add(" ");
        for (int i = 0; i < portfolio.size(); i++) {
            stockList.add(portfolio.getStock(i).getStockSymbol());
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(stockList.toArray());

        comboBox = new JComboBox(model);

        comboBox.setActionCommand(COMBO);
        comboBox.addActionListener(this);

        setLeftAndSize(comboBox);
        this.add(comboBox);
    }

    // MODIFIES: this
    // EFFECTS: adds the spinner for inputting the number of stocks to sell
    private void addSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, null, 1);
        spinner = new JSpinner(model);

        JFormattedTextField txt = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
        ((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

        setLeftAndSize(spinner);
        this.add(spinner);
    }

    // MODIFIES: this
    // EFFECTS: adds the field for inputting selling price
    private void addPriceField() {
        NumberFormatter formatter = new NumberFormatter();
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.1);
        formatter.setAllowsInvalid(false);
        priceField = new JFormattedTextField(formatter);

        priceField.setValue(null);
        setLeftAndSize(priceField);
        this.add(priceField);
    }

    // MODIFIES: this
    // EFFECTS: refreshes the combo box showing stocks available to sell
    public void updateComboBox() {
        Portfolio portfolio = accountFrontPanel.getAccount().getPortfolio();

        List<String> stockList = new ArrayList<>();
        stockList.add(" ");
        for (int i = 0; i < portfolio.size(); i++) {
            stockList.add(portfolio.getStock(i).getStockSymbol());
        }
        DefaultComboBoxModel newModel = new DefaultComboBoxModel(stockList.toArray());
        comboBox.setModel(newModel);
    }

    // MODIFIES: this
    // EFFECTS: regularly refreshes the price label of the selected stock
    private void updateComboBoxMessage() {
        if (timer != null) {
            timer.stop();
        }
        String symbol = (String) comboBox.getSelectedItem();

        Market market = accountFrontPanel.getLoginGUI().getFrontGUI().getMarket();
        Stock stock = market.getStock(symbol);
        StockOwned stockOwned = accountFrontPanel.getAccount().getPortfolio().getStock(symbol);

        if (stock != null) {
            comboBoxMessage.setText("Current bid price = " + String.format("$%.2f", stock.getBidPrice()));
            averageCostMessage.setText("Your average purchase cost was "
                    + String.format("$%.2f", stockOwned.getAverageCost()));

            timer = new Timer(FrontGUI.everyMillisecondUpdateStockPrice, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    comboBoxMessage.setText("Current bid price = " + String.format("$%.2f", stock.getBidPrice()));
                }
            });
            timer.start();
        } else {
            comboBoxMessage.setText(" ");
            averageCostMessage.setText(" ");
        }
    }

    // MODIFIES: this
    // EFFECTS: updates the max number of shares available to sell for the selected stock
    private void updateSpinnerMax() {
        spinner.setValue(1);
        String symbol = (String) comboBox.getSelectedItem();
        if (!symbol.equals(" ")) {
            StockOwned stockOwned = accountFrontPanel.getAccount().getPortfolio().getStock(symbol);
            int numSharesOwned = stockOwned.getNumSharesOwned();
            SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
            model.setMaximum(numSharesOwned);
        }
    }

    // MODIFIES: this, accountFrontPanel
    // EFFECTS: sell the stock
    private void sell(String symbol, int numSharesToSell, Double price) {
        Market market = accountFrontPanel.getLoginGUI().getFrontGUI().getMarket();
        Stock stock = market.getStock(symbol);
        Account account = accountFrontPanel.getAccount();
        StockOwned stockOwned = account.getPortfolio().getStock(symbol);

        if (symbol.equals(" ")) {
            sellMessage.setText("No stock selected.");
        } else if (price == null) {
            sellMessage.setText("No price entered.");
        } else if (price > stock.getAskPrice()) {
            sellMessage.setText("Your price needs to be <= current bid price, please try again.");
        } else {
            account.sell(stockOwned, numSharesToSell, price);
            sellMessage.setText("Successfully sold " + numSharesToSell + " X "
                    + symbol + " @ " + (String.format("$%.2f", price)));
            updateComboBox();
            updateComboBoxMessage();
            updateSpinnerMax();
            priceField.setValue(null);
        }
    }

    // MODIFIES: this
    // EFFECTS: resets fields and labels on this panel, and stop the refreshing of label
    @Override
    protected void resetFieldsAndLabels() {
        comboBox.setSelectedIndex(0);
        comboBoxMessage.setText(" ");
        averageCostMessage.setText(" ");
        spinner.setValue(1);
        priceField.setValue(null);
        sellMessage.setText(" ");
        if (timer != null) {
            timer.stop();
        }
    }

    // MODIFIES: this
    // EFFECTS: handles actions performed on this panel
    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout) accountFrontPanel.getLayout();
        String command = e.getActionCommand();

        switch (command) {
            case COMBO:
                updateComboBoxMessage();
                updateSpinnerMax();
                break;
            case SELL:
                String symbol = (String) comboBox.getSelectedItem();
                int numSharesToBuy = (Integer) spinner.getValue();
                Double price = (Double) priceField.getValue();
                sell(symbol, numSharesToBuy, price);
                break;
            case BACK:
                cl.show(accountFrontPanel, AccountFrontPanel.ACCOUNT_FRONT);
                resetFieldsAndLabels();
                break;
        }
    }
}
