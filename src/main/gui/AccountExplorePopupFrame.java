package gui;

import model.Stock;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AccountExplorePopupFrame extends JFrame {

    private Stock stock;
    private ImageIcon companyLogo;
    private JTable table;
    private Timer timer;

    public AccountExplorePopupFrame(Stock stock) {
        super(stock.getSymbol());
        this.stock = stock;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        BoxLayout bl = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        this.setLayout(bl);

        companyLogo = new ImageIcon("./data/" + stock.getSymbol() + ".png");
        Image image = companyLogo.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel imageAsLabel = new JLabel(new ImageIcon(image));
        imageAsLabel.setAlignmentX(CENTER_ALIGNMENT);
        this.add(imageAsLabel);

        setUpTable();
        this.add(new JScrollPane(table));

        setSize(400, 400);
        this.setLocationRelativeTo(null);
        setVisible(true);

    }

    private void setUpTable() {
        DefaultTableModel model = new DefaultTableModel();
        Object[] headings = {"Stock symbol", "Company", "Sector", "Bid price", "Ask price", "Market Capitalization",
                "P/E Ratio", "Beta", "Earnings per share", "Total Shares"};
        Object[] data = {stock.getSymbol(), stock.getName(), stock.getSector(),
                String.format("$%,.2f", stock.getBidPrice()), String.format("$%,.2f", stock.getAskPrice()),
                String.format("$%,.0f", stock.marketCap() / 1e6) + "M", String.format("%,.2f", stock.peRatio()),
                stock.getBeta(), stock.getEarningsPerShare(), stock.getTotalNumShares() / 1e6 + "M"};
        model.addColumn(null, headings);
        model.addColumn(null, data);

        table = new JTable(model);
        table.getTableHeader().setVisible(false);
        table.setDefaultEditor(Object.class, null);

        timer = new Timer(FrontGUI.everyMillisecondUpdateStockPrice, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setValueAt(String.format("$%,.2f", stock.getBidPrice()),3,1);
                model.setValueAt(String.format("$%,.2f", stock.getAskPrice()),4,1);
                model.setValueAt(String.format("$%,.0f", stock.marketCap() / 1e6) + "M",5,1);
                model.setValueAt(String.format("%,.2f", stock.peRatio()),6,1);
            }
        });
        timer.start();
    }

}
