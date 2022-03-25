package gui;

import model.Account;
import model.Accounts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FrontNewAccountPanel extends AbstractPanel {

    protected static final String BACK_FRONT = "backFront";
    protected static final String REGISTER = "register";

    private FrontGUI frontGUI;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JLabel usernameUsedLabel;
    private JLabel registrationMessageLabel;

    public FrontNewAccountPanel(FrontGUI frontGUI) {
        super();
        this.frontGUI = frontGUI;
        addUsernameFieldAndLabels();
        addPasswordFieldAndLabels();
        addNameField();
        addRegistrationMessageLabel();
        addButton("Register", REGISTER);
        addButton("Back", BACK_FRONT);
    }

    private void addUsernameFieldAndLabels() {
        addStaticLabel("Register a new account:");
        addStaticLabel("Username (not case-sensitive):");
        usernameField = new JTextField();
        setLeftAndSize(usernameField);
        this.add(usernameField);
        usernameUsedLabel = new JLabel(" ");
        usernameUsedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(usernameUsedLabel);
    }

    private void addPasswordFieldAndLabels() {
        addStaticLabel("Password (case-sensitive):");
        passwordField = new JPasswordField();
        setLeftAndSize(passwordField);
        this.add(passwordField);
    }

    private void addNameField() {
        addStaticLabel("Your name:");
        nameField = new JTextField();
        setLeftAndSize(nameField);
        this.add(nameField);
    }

    private void addRegistrationMessageLabel() {
        registrationMessageLabel = new JLabel(" ");
        registrationMessageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(registrationMessageLabel);
    }

    private void registerAccount(String username, String password, String name) {
        Accounts accounts = this.frontGUI.getAccounts();
        if (isEmptyOrContainsSpace(username) || isEmptyOrContainsSpace(password) || isEmptyOrContainsSpace(name)) {
            usernameUsedLabel.setText(" ");
            registrationMessageLabel.setText("Input cannot be empty or contain space, please try again.");
        } else if (accounts.isUsernameUsedAlready(username)) {
            usernameUsedLabel.setText("Username is used already, please try again.");
            registrationMessageLabel.setText(" ");
        } else {
            Account newAccount = new Account(username, password, name);
            accounts.addAccount(newAccount);
            usernameUsedLabel.setText(" ");
            registrationMessageLabel.setText("Registration success! You can go back and login now.");
        }
    }

    private boolean isEmptyOrContainsSpace(String string) {
        return string.isEmpty() || string.contains(" ");
    }

    protected void resetFieldsAndLabels() {
        usernameField.setText(null);
        passwordField.setText(null);
        nameField.setText(null);
        usernameUsedLabel.setText(" ");
        registrationMessageLabel.setText(" ");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Container containerPanel = frontGUI.getContainerPanel();
        CardLayout cl = (CardLayout) containerPanel.getLayout();
        String command = e.getActionCommand();

        switch (command) {
            case REGISTER:
                registerAccount(usernameField.getText(),
                        new String(passwordField.getPassword()), nameField.getText());
                break;
            case BACK_FRONT:
                resetFieldsAndLabels();
                cl.show(containerPanel, FrontGUI.FRONT);
                break;
        }
    }
}
