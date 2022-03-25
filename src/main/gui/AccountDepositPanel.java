package gui;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AccountDepositPanel extends AbstractPanel {

    protected static final String DEPOSIT = "deposit";

    private AccountFrontPanel accountFrontPanel;
    private JFormattedTextField amountField;
    private JLabel messageLabel;

    public AccountDepositPanel(AccountFrontPanel accountFrontPanel) {
        super();
        this.accountFrontPanel = accountFrontPanel;
        addStaticLabel("Amount to deposit:");
        addAmountField();
        addMessageLabel();
        addButton("Deposit", DEPOSIT);
        addButton("Back", BACK);
    }

    private void addAmountField() {
        NumberFormatter formatter = new NumberFormatter();
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.01);
        formatter.setAllowsInvalid(false);

        amountField = new JFormattedTextField(formatter);
        setLeftAndSize(amountField);
        this.add(amountField);
    }

    private void addMessageLabel() {
        messageLabel = new JLabel(" ");
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(messageLabel);
    }

    private void deposit(Double amount) {
        if (amount != null) {
            accountFrontPanel.getAccount().deposit(amount);
            messageLabel.setText("Deposit success.");
        } else {
            messageLabel.setText("No amount is entered, please try again.");
        }
    }

    @Override
    protected void resetFieldsAndLabels() {
        amountField.setValue(null);
        messageLabel.setText(" ");
    }

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
