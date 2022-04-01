package gui;

import model.Accounts;
import model.Event;
import model.EventLog;
import model.Market;
import model.Stock;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

// Represents the GUI frame of the BeefStock app
public class FrontGUI extends JFrame implements ActionListener {

    //For GUI
    protected static final String FRONT = "front";
    protected static final String NEW_ACCOUNT = "new";
    protected static final String LOGIN = "login";
    protected static final String SAVE = "save";
    protected static final String LOAD = "load";
    protected static final String EXIT = "exit";
    protected static final Border MARGIN = new EmptyBorder(10, 10, 10, 10);

    private JPanel containerPanel; // for containing various panels
    private JPanel frontPanel;
    private FrontNewAccountPanel newAccountPanel;
    private FrontLoginPanel loginPanel;
    private JLabel saveLoadLabel;

    //For models
    protected static final int everyMillisecondUpdateStockPrice = 2000;
    private static final String PATH = "./data/MainSave.json";

    private Market market;
    private Accounts accounts;
    private Timer timer;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs and setups the JFrame of the application
    public FrontGUI() {
        super("Beef Stock");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700, 700);
        setUp();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets up the panels and underlying models
    private void setUp() {
        setUpPanels();
        setUpModels();
    }

    // MODIFIES: this
    // EFFECTS: sets up card panel and its corresponding panels
    private void setUpPanels() {
        containerPanel = new JPanel(new CardLayout());

        setUpFrontPanel();
        containerPanel.add(frontPanel, FRONT);

        newAccountPanel = new FrontNewAccountPanel(this);
        containerPanel.add(newAccountPanel, NEW_ACCOUNT);

        loginPanel = new FrontLoginPanel(this);
        containerPanel.add(loginPanel, LOGIN);

        this.add(containerPanel);
    }

    // MODIFIES: this
    // EFFECTS: sets up front panel
    private void setUpFrontPanel() {
        frontPanel = new JPanel();
        setBoxLayoutVertical(frontPanel);
        Border border = frontPanel.getBorder();
        frontPanel.setBorder(new CompoundBorder(border, MARGIN));
        JLabel welcomeMessage = new JLabel("<html>Welcome to Beef Stock,<br/>"
                + "your wallet-friendly stock trading platform</html>");
        welcomeMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        frontPanel.add(welcomeMessage);
        addFrontButtons();
        saveLoadLabel = new JLabel(" ");
        saveLoadLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        frontPanel.add(saveLoadLabel);
    }

    // MODIFIES: this
    // EFFECTS: add all related buttons to front panel
    private void addFrontButtons() {
        addButton("Create new account", NEW_ACCOUNT);
        addButton("Login", LOGIN);
        addButton("Save accounts to file", SAVE);
        addButton("Load accounts from file", LOAD);
        addButton("Exit", EXIT);
    }

    // MODIFIES: this
    // EFFECTS: sets up and adds a button with action listener to front panel
    private void addButton(String text, String command) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        frontPanel.add(button);
    }

    // MODIFIES: panel
    // EFFECTS: sets box layout for panel
    private void setBoxLayoutVertical(Container panel) {
        BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(bl);
    }

    // EFFECTS: returns container panel
    public Container getContainerPanel() {
        return containerPanel;
    }

    // MODIFIES: this
    // EFFECTS: clears relevant labels and fields on front panel
    private void clearLabelsAndFields() {
        saveLoadLabel.setText(" ");
    }

    // MODIFIES: this
    // EFFECTS: handles action events on this frame
    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout) containerPanel.getLayout();
        String command = e.getActionCommand();
        switch (command) {
            case NEW_ACCOUNT:
                cl.show(containerPanel, NEW_ACCOUNT);
                clearLabelsAndFields();
                break;
            case LOGIN:
                cl.show(containerPanel, LOGIN);
                clearLabelsAndFields();
                break;
            case SAVE:
                saveAccounts();
                break;
            case LOAD:
                loadAccounts();
                break;
            default:
                timer.cancel();
                this.dispose();
                printLog(EventLog.getInstance());
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: sets up underlying models for the application
    private void setUpModels() {
        market = new Market();
        addStocksToMarket();

        accounts = new Accounts();

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
    // EFFECTS: loads all stocks into the market
    private void addStocksToMarket() {
        market.addStock(new Stock("AAPL", "Apple Inc.", 176.28, 0.0005,
                16320000000L, 6.01, 1.19, "Technology"));
        market.addStock(new Stock("MSFT", "Microsoft Corporation", 311.21, 0.0003,
                7500000000L, 9.39, 0.89, "Technology"));
        market.addStock(new Stock("GOOG", "Alphabet Inc.", 2829.06, 0.0007,
                315640000L, 112.2, 1.07, "Technology"));
        market.addStock(new Stock("TSLA", "Tesla, Inc.", 932.00, 0.0008,
                1030000000L, 4.9, 2.01, "Capital Goods"));
    }

    // MODIFIES: this
    // EFFECTS: load accounts from PATH to application
    private void loadAccounts() {
        jsonReader = new JsonReader(PATH);
        try {
            accounts = jsonReader.read();
            saveLoadLabel.setText("Accounts data loaded.");
        } catch (IOException e) {
            //
        }
    }

    // MODIFIES: this
    // EFFECTS: save existing state of accounts to PATH
    private void saveAccounts() {
        jsonWriter = new JsonWriter(PATH);
        try {
            jsonWriter.open();
            jsonWriter.write(accounts);
            jsonWriter.close();
            saveLoadLabel.setText("Accounts data saved.");
        } catch (FileNotFoundException e) {
            //
        }
    }

    private void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.toString());
        }
    }


    // EFFECTS: returns accounts
    public Accounts getAccounts() {
        return accounts;
    }

    // EFFECTS: returns market
    public Market getMarket() {
        return market;
    }
}
