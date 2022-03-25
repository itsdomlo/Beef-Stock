package gui;

import model.Account;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;

// Represents the withdrawal panel
public class AccountWithdrawPanel extends AbstractPanel {

    protected static final String WITHDRAW = "withdraw";

    private AccountFrontPanel accountFrontPanel;
    private JFormattedTextField amountField;
    private JLabel messageLabel;

    // EFFECTS: constructs and sets up the withdrawal panel
    public AccountWithdrawPanel(AccountFrontPanel accountFrontPanel) {
        super();
        this.accountFrontPanel = accountFrontPanel;
        addStaticLabel("Amount to withdraw:");
        addAmountField();
        addMessageLabel();
        addButton("Withdraw", WITHDRAW);
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
    // EFFECTS: withdraws amount into account
    private void withdraw(Double amount) {
        Account account = accountFrontPanel.getAccount();
        if (amount != null) {
            if (amount <= account.getBalance()) {
                account.withdraw(amount);
                messageLabel.setText("Withdraw success.");
            } else {
                messageLabel.setText("Insufficient account balance, please try again.");
            }
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
            case WITHDRAW:
                withdraw((Double) amountField.getValue());
                break;
            case BACK:
                resetFieldsAndLabels();
                cl.show(accountFrontPanel, AccountFrontPanel.ACCOUNT_FRONT);
                break;
        }

    }

}
