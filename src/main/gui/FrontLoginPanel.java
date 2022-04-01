package gui;

import model.Account;
import model.Accounts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the login panel
public class FrontLoginPanel extends JPanel implements ActionListener {


    protected static final String BACK_FRONT = "backFront";
    protected static final String TRY_LOGIN = "tryLogin";

    protected static final String LOGIN_PANEL = "loginPanel";
    protected static final String ACCOUNT_PANEL = "accountPanel";

    private FrontGUI frontGUI;
    private JPanel loginPanel;
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JLabel loginFailLabel = new JLabel(" ");

    // EFFECTS: constructs itself as a card panel which contains the login panel
    public FrontLoginPanel(FrontGUI frontGUI) {
        super(new CardLayout());
        this.frontGUI = frontGUI;

        setUpLoginPanel();
        this.add(loginPanel,LOGIN_PANEL);
    }

    // MODIFIES: this
    // EFFECTS: sets up the login panel
    private void setUpLoginPanel() {
        loginPanel = new JPanel();

        BoxLayout bl = new BoxLayout(loginPanel, BoxLayout.Y_AXIS);
        loginPanel.setLayout(bl);
        Border border = loginPanel.getBorder();
        loginPanel.setBorder(new CompoundBorder(border, FrontGUI.MARGIN));

        addFieldsAndLabels();
        addButton("Login", TRY_LOGIN);
        addButton("Back", BACK_FRONT);
    }

    // MODIFIES: this
    // EFFECTS: sets up components of the login panel
    private void addFieldsAndLabels() {
        addStaticLabel("Login:");
        addStaticLabel("Username:");
        setLeftAndSize(usernameField);
        loginPanel.add(usernameField);
        addStaticLabel("Password:");
        setLeftAndSize(passwordField);
        loginPanel.add(passwordField);
        loginFailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(loginFailLabel);
    }

    // MODIFIES: this
    // EFFECTS: adds a button with action listener to login panel
    protected void addButton(String text, String command) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(button);
    }

    // MODIFIES: this
    // EFFECTS: adds a label to the login panel
    protected void addStaticLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPanel.add(label);
    }

    // MODIFIES: c
    // EFFECTS: set size and left align c
    protected void setLeftAndSize(JComponent c) {
        c.setMaximumSize(new Dimension(200, c.getPreferredSize().height));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // MODIFIES: this
    // EFFECTS: resets relevant fields and labels on the login panel
    protected void resetFieldsAndLabels() {
        usernameField.setText(null);
        passwordField.setText(null);
        loginFailLabel.setText(" ");
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a logged in account panel to this, and show it
    private void login(String username, String password) {
        Accounts accounts = frontGUI.getAccounts();
        Account account = accounts.getAccount(username, password);
        if (account != null) {
            AccountFrontPanel accountFrontPanel = new AccountFrontPanel(this,account);
            this.add(accountFrontPanel, ACCOUNT_PANEL);
            CardLayout cl = (CardLayout) this.getLayout();
            cl.show(this, ACCOUNT_PANEL);
            resetFieldsAndLabels();
        } else {
            loginFailLabel.setText("Incorrect username or password, please try again.");
        }

    }

    // MODIFIES: this
    // EFFECTS: handles action events on this panel
    @Override
    public void actionPerformed(ActionEvent e) {
        Container containerPanel = frontGUI.getContainerPanel();
        CardLayout cl = (CardLayout) containerPanel.getLayout();
        String command = e.getActionCommand();

        switch (command) {
            case TRY_LOGIN:
                login(usernameField.getText(),new String(passwordField.getPassword()));
                break;
            case BACK_FRONT:
                resetFieldsAndLabels();
                cl.show(containerPanel, FrontGUI.FRONT);
                break;
        }
    }

    // EFFECTS: returns frontGUI
    public FrontGUI getFrontGUI() {
        return frontGUI;
    }

}
