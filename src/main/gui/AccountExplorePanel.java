package gui;

import model.Market;
import model.Stock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;


public class AccountExplorePanel extends AbstractPanel {

    private AccountFrontPanel accountFrontPanel;
    private JTable table;
    private Timer timer;

    public AccountExplorePanel(AccountFrontPanel accountFrontPanel) {
        super();
        this.accountFrontPanel = accountFrontPanel;
        addStaticLabel("You can trade the following stocks. Double click any for details.");
        addTable();
        addButton("Back", BACK);
    }

    private void addTable() {
        Market market = accountFrontPanel.getLoginGUI().getFrontGUI().getMarket();

        Object[] headings = {"Symbol", "Company name", "Bid price", "Ask price"};
        DefaultTableModel model = new DefaultTableModel(headings, 0);
        for (int i = 0; i < market.size(); i++) {
            Stock stock = market.getStock(i);
            Object[] rowData = {stock.getSymbol(), stock.getName(),
                    String.format("$%,.2f", stock.getBidPrice()), String.format("$%,.2f", stock.getAskPrice())};
            model.addRow(rowData);
        }

        table = new JTable(model);
        table.setDefaultEditor(Object.class, null);

        doubleClickForDetails(market);

        JScrollPane sp = new JScrollPane(table);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(sp);
    }

    private void doubleClickForDetails(Market market) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Point point = e.getPoint();
                    int row = table.rowAtPoint(point);
                    String symbol = (String) table.getValueAt(row, 0);
                    Stock stock = market.getStock(symbol);
                    JFrame popup = new AccountExplorePopupFrame(stock);
                }
            }
        });
    }

    public void regularRefreshData() {
        Market market = accountFrontPanel.getLoginGUI().getFrontGUI().getMarket();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        timer = new Timer(FrontGUI.everyMillisecondUpdateStockPrice, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < market.size(); i++) {
                    Stock stock = market.getStock(i);
                    model.setValueAt(String.format("$%,.2f", stock.getBidPrice()), i, 2);
                    model.setValueAt(String.format("$%,.2f", stock.getAskPrice()), i, 3);
                }
            }
        });
        timer.start();
    }

    @Override
    protected void resetFieldsAndLabels() {

    }

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
