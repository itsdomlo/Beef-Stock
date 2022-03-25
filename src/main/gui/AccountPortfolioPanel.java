package gui;

import model.Market;
import model.Portfolio;
import model.StockOwned;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the account portfolio panel
public class AccountPortfolioPanel extends AbstractPanel {

    private AccountFrontPanel accountFrontPanel;
    private JTable table;
    private Timer timer;

    // EFFECTS: sets up the account portfolio panel
    public AccountPortfolioPanel(AccountFrontPanel accountFrontPanel) {
        super();
        this.accountFrontPanel = accountFrontPanel;
        addStaticLabel("Here's your portfolio:");
        addTable();
        addButton("Back", BACK);
    }

    // MODIFIES: this
    // EFFECTS: adds an empty table
    private void addTable() {
        table = new JTable();
        table.setDefaultEditor(Object.class, null);

        JScrollPane sp = new JScrollPane(table);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(sp);
    }

    // MODIFIES: this
    // EFFECTS: loads the data model into table
    public void setUpTableModel() {
        Market market = accountFrontPanel.getLoginGUI().getFrontGUI().getMarket();
        Portfolio portfolio = accountFrontPanel.getAccount().getPortfolio();

        Object[] headings = {"Symbol", "# shares", "Average cost", "Total cost",
                "Total market value", "Profit / Loss"};
        DefaultTableModel model = new DefaultTableModel(headings, 0);
        for (int i = 0; i < portfolio.size(); i++) {
            StockOwned stockOwned = portfolio.getStock(i);
            String symbol = stockOwned.getStockSymbol();
            double averageValue = stockOwned.totalValueAtAverageCost();
            double marketValue = market.stockValueAtMarketPrice(symbol, stockOwned.getNumSharesOwned());

            Object[] rowData = {symbol, stockOwned.getNumSharesOwned(),
                    String.format("$%,.2f", stockOwned.getAverageCost()), String.format("$%,.2f", averageValue),
                    String.format("$%,.2f", marketValue), String.format("$%,.2f", marketValue - averageValue)};
            model.addRow(rowData);
        }
        table.setModel(model);
    }

    // MODIFIES: this
    // EFFECTS: regularly refreshes the data model
    public void regularRefreshData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Market market = accountFrontPanel.getLoginGUI().getFrontGUI().getMarket();
        Portfolio portfolio = accountFrontPanel.getAccount().getPortfolio();

        timer = new Timer(FrontGUI.everyMillisecondUpdateStockPrice, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < portfolio.size(); i++) {
                    StockOwned stockOwned = portfolio.getStock(i);
                    String symbol = stockOwned.getStockSymbol();
                    double averageValue = stockOwned.totalValueAtAverageCost();
                    double marketValue = market.stockValueAtMarketPrice(symbol, stockOwned.getNumSharesOwned());
                    model.setValueAt(String.format("$%,.2f", marketValue), i, 4);
                    model.setValueAt(String.format("$%,.2f", marketValue - averageValue), i, 5);
                }
            }
        });
        timer.start();
    }

    @Override
    protected void resetFieldsAndLabels() {
        // nothing to resets
    }

    // MODIFIES: this
    // EFFECTS: handles actions performed on this panel
    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout) accountFrontPanel.getLayout();
        String command = e.getActionCommand();

        switch (command) {
            case BACK:
                cl.show(accountFrontPanel, AccountFrontPanel.ACCOUNT_FRONT);
                timer.stop();
                break;
        }
    }
}
