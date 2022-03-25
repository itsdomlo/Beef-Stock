package gui;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;

// Represents the deposit panel
public class AccountDepositPanel extends AbstractPanel {

    protected static final String DEPOSIT = "deposit";

    private AccountFrontPanel accountFrontPanel;
    private JFormattedTextField amountField;
    private JLabel messageLabel;

    // EFFECTS: constructs and sets up the deposit panel
    public AccountDepositPanel(AccountFrontPanel accountFrontPanel) {
        super();
        this.accountFrontPanel = accountFrontPanel;
        addStaticLabel("Amount to deposit:");
        addAmountField();
        addMessageLabel();
        addButton("Deposit", DEPOSIT);
        addButton("Back", BACK);
    }

    // MODIFIES: this
    // EFFECTS: sets up and adds the amount field
    private void addAmountField() {
        NumberFormatter formatter = new NumberFormatter();
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.01);
        formatter.setAllowsInvalid(false);

        amountField = new JFormattedTextField(formatter);
        setLeftAndSize(amountField);
        this.add(amountField);
    }

    // MODIFIES: this
    // EFFECTS: sets up and adds the message label
    private void addMessageLabel() {
        messageLabel = new JLabel(" ");
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(messageLabel);
    }

    // MODIFIES: this, accountFrontPanel
    // EFFECTS: deposits amount into account
    private void deposit(Double amount) {
        if (amount != null) {
            accountFrontPanel.getAccount().deposit(amount);
            messageLabel.setText("Deposit success.");
        } else {
            messageLabel.setText("No amount is entered, please try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: resets fields and labels on this panel
    @Override
    protected void resetFieldsAndLabels() {
        amountField.setValue(null);
        messageLabel.setText(" ");
    }

    // MODIFIES: this
    // EFFECTS: handles actions performed on this panel
    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cl = (CardLayout) accountFrontPanel.getLayout();
        String command = e.getActionCommand();

        switch (command) {
            case DEPOSIT:
                deposit((Double) amountField.getValue());
                break;
            case BACK:
                resetFieldsAndLabels();
                cl.show(accountFrontPanel, AccountFrontPanel.ACCOUNT_FRONT);
                break;
        }

    }
}
