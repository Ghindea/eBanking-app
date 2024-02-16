package org.poo.cb;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;
import java.util.Currency;

public class User implements Observer{

    private String email, firstName, lastName, address;
    private List<BankAccount> accounts;
    private List<legalGambling> stocks;
    private List<String> notifications;

    @Override
    public void update(String message) {
        notifications.add("new Notification: " + message);
    }

    public static class legalGambling {
        int noStocks;
        String StockName;
        legalGambling(String StockName, int noStocks) {
            this.noStocks = noStocks;
            this.StockName = StockName;
        }
        public String toString() {
            return "{\"StockName\":\"" + StockName + "\",\"amount\":" + noStocks + "}";
        }
    }
    private List<String> friends;
    User(UserBuilder builder) {
        this.friends         = new ArrayList<>();
        this.accounts        = new ArrayList<>();
        this.stocks          = new ArrayList<>();
        this.notifications   = new ArrayList<>();
        this.email      = builder.email;
        this.lastName   = builder.lastName;
        this.firstName  = builder.firstName;
        this.address    = builder.address;
    }
    public static class UserBuilder {
        private String email, lastName, firstName, address;
        // TODO accounts, stocks & friends

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }
        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public UserBuilder setAddress(String address) {
            this.address = address;
            return this;
        }
        public User build() {
            return new User(this);
        }
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }
    public BankAccount getAccount(CurrencyEnum currency) {
        for (BankAccount b : accounts) {
            if (b.getCurrency().equals(currency))
                return b;
        }
        return null;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    @Override
    public String toString() {
        String s = "{" +
                "\"email\":\"" + email + "\"," +
                "\"firstName\":\"" + firstName + "\"," +
                "\"lastName\":\"" + lastName + "\"," +
                "\"address\":\"" + address + "\"," +
                "\"friends\":[";
        if (!friends.isEmpty()) {
            for (int i = 0; i < friends.size()-1; i++) {
                s += "\"" + friends.get(i)+ "\", ";
            }
            s += "\"" +friends.get(friends.size()-1) + "\"";
        }
        s += "]}";

        return s;
    }
    public String portofolioToString() {
        String s = "{" +
                "\"stocks\":" + stocks.toString() + "," +
                "\"accounts\":" + accounts.toString() +
                "}";
        return s;
    }
    public void addFriend(User user) {
        friends.add(user.getEmail());
    }
    public Boolean isFriendWith(String friend) {
        for (String f : friends) {
            if (f.equals(friend))
                return true;
        }
        return false;
    }
    public void addAccount(BankAccount account) {accounts.add(account);}
    public void addDinero(CurrencyEnum c, Double amount) {
        for (BankAccount account : accounts) {
            if (account.getCurrency().equals(c)) {
                account.addMoney(amount);
            }
        }
    }
    public void addStock(Stock stock, int noStocks) {
        stocks.add(new legalGambling(stock.getCompanyName(), noStocks));
    }
}
