package gui;

import model.Account;
import model.Market;
import model.Stock;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AccountBuyPanel extends AbstractPanel {

    protected static final String COMBO = "combo";
    protected static final String BUY = "buy";

    private AccountFrontPanel accountFrontPanel;
    private JComboBox comboBox;
    private JLabel comboBoxMessage;
    private JSpinner spinner;
    private JFormattedTextField priceField;
    private JLabel buyMessage;
    private Timer timer;

    public AccountBuyPanel(AccountFrontPanel accountFrontPanel) {
        super();
        this.accountFrontPanel = accountFrontPanel;
        addStaticLabel("Choose a stock to buy:");
        addComboBox();
        comboBoxMessage = new JLabel(" ");
        this.add(comboBoxMessage);
        addStaticLabel(" ");
        addStaticLabel("Number to buy:");
        addSpinner();
        addStaticLabel("Purchase price per stock:");
        addPriceField();
        buyMessage = new JLabel(" ");
        this.add(buyMessage);
        addStaticLabel(" ");
        addButton("Buy", BUY);
        addButton("Back", BACK);
    }

    private void addComboBox() {
        List<String> stockList = new ArrayList<>();
        stockList.add(" ");
        Market market = accountFrontPanel.getLoginGUI().getFrontGUI().getMarket();
        for (int i = 0; i < market.size(); i++) {
            stockList.add(market.getStock(i).getSymbol());
        }
        comboBox = new JComboBox(stockList.toArray());

        comboBox.setActionCommand(COMBO);
        comboBox.addActionListener(this);

        setLeftAndSize(comboBox);
        this.add(comboBox);
    }

    private void addSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, null, 1);
        spinner = new JSpinner(model);

        JFormattedTextField txt = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
        ((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

        setLeftAndSize(spinner);
        this.add(spinner);
    }

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

    private void updateComboBoxMessage() {
        Market market = accountFrontPanel.getLoginGUI().getFrontGUI().getMarket();

        if (timer != null) {
            timer.stop();
        }
        String symbol = (String) comboBox.getSelectedItem();
        Stock stock = market.getStock(symbol);
        if (stock != null) {
            comboBoxMessage.setText("Current ask price = "
                    + String.format("$%.2f", stock.getAskPrice()));
            timer = new Timer(FrontGUI.everyMillisecondUpdateStockPrice, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    comboBoxMessage.setText("Current ask price = "
                            + String.format("$%.2f", stock.getAskPrice()));
                }
            });
            timer.start();
        } else {
            comboBoxMessage.setText(" ");
        }
    }

    private void buy(String symbol, int numSharesToBuy, Double price) {
        Market market = accountFrontPanel.getLoginGUI().getFrontGUI().getMarket();
        Account account = accountFrontPanel.getAccount();
        Stock stock = market.getStock(symbol);

        if (symbol.equals(" ")) {
            buyMessage.setText("No stock selected.");
        } else if (price == null) {
            buyMessage.setText("No price entered.");
        } else if (price < stock.getAskPrice()) {
            buyMessage.setText("Your price needs to be >= current ask price, please try again.");
        } else {
            double totalCost = numSharesToBuy * price + account.getFeePerTrade();
            double balance = account.getBalance();
            if (totalCost > balance) {
                buyMessage.setText("You have insufficient balance, please try again.");
            } else {
                account.buy(symbol, numSharesToBuy, price);
                buyMessage.setText("Successfully purchased " + numSharesToBuy + " X "
                        + symbol + " @ " + (String.format("$%.2f", price)));
            }
        }
    }

    @Override
    protected void resetFieldsAndLabels() {
        comboBox.setSelectedIndex(0);
        comboBoxMessage.setText(" ");
        spinner.setValue(1);
        priceField.setValue(null);
        buyMessage.setText(" ");
        if (timer != null) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout) accountFrontPanel.getLayout();
        String command = e.getActionCommand();

        switch (command) {
            case COMBO:
                updateComboBoxMessage();
                break;
            case BUY:
                String symbol = (String) comboBox.getSelectedItem();
                int numSharesToBuy = (Integer) spinner.getValue();
                Double price = (Double) priceField.getValue();
                buy(symbol, numSharesToBuy, price);
                break;
            case BACK:
                cl.show(accountFrontPanel, AccountFrontPanel.ACCOUNT_FRONT);
                resetFieldsAndLabels();
                break;
        }
    }
}
