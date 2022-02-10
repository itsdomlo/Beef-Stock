package model;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

// Represents the account database of the trading app
public class Accounts {

    private List<Account> database;

    public Accounts() {
        database = new ArrayList<>();
    }

    // EFFECTS: returns true if username is already used in an account in database
    public boolean isUsernameUsedAlready(String username) {
        for (Account account : this.database) {
            if (account.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns account with the correct username and password
    public Account getAccount(String username, String password) {
        for (Account account : this.database) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }

    // REQUIRES: username is unique
    // MODIFIES: this
    // EFFECTS: creates new account and add to database
    public void addAccount(Account account) {
        database.add(account);
    }

}
