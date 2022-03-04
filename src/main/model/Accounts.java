package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents the account database of the trading app, in which the accounts must have unique username
public class Accounts implements Writable {

    private List<Account> database;

    // EFFECTS: initialize an empty account database
    public Accounts() {
        database = new ArrayList<>();
    }

    // REQUIRES: username is unique
    // MODIFIES: this
    // EFFECTS: creates new account and add to database
    public void addAccount(Account account) {
        database.add(account);
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

    // EFFECTS: returns Account at index
    public Account getAccount(int index) {
        return this.database.get(index);
    }

    // EFFECTS: returns number of accounts in database
    public int size() {
        return this.database.size();
    }

    // EFFECTS: returns true if database is empty, false otherwise
    public boolean isEmpty() {
        return this.database.isEmpty();
    }

    // EFFECTS: returns accounts as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("database",databaseToJson());
        return json;
    }

    // EFFECTS: returns all accounts in this database as a JSON array
    private JSONArray databaseToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Account account : database) {
            jsonArray.put(account.toJson());
        }

        return jsonArray;
    }

}
