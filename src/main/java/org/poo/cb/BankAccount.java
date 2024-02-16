package org.poo.cb;

import org.jetbrains.annotations.NotNull;
import org.poo.cb.exceptions.InsufficientAmountException;

import java.util.ArrayList;
import java.util.List;

public class BankAccount implements Comparable<BankAccount>, Subject {
    private CurrencyEnum currency;
    private Double balance;
    private List<User> observers;
    BankAccount(CurrencyEnum currencyEnum) {
        this.currency = currencyEnum;
        this.balance  = 0.0;
        observers     = new ArrayList<>();
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public int compareTo(@NotNull BankAccount o) {
        return this.currency.compareTo(o.currency);
    }
    public void addMoney(Double amount) {
        this.balance += amount;
    }
    public void takeMoney(Double amount) throws InsufficientAmountException {
        if (amount > balance)
            throw new InsufficientAmountException(currency.toString(), true);
        this.balance -= amount;
    }

    @Override
    public String toString() {
        return "{" +
                "\"currencyname\":\"" + currency.toString() + "\"," +
                "\"amount\":\"" + String.format("%.2f", balance)+ "\"" +
                '}';
    }

    @Override
    public void addObserver(User u) {
        observers.add(u);
    }

    @Override
    public void removeObserver(User u) {
        observers.remove(u);
    }

    @Override
    public void notify(String message) {
        for (User u : observers) {
            u.update(message);
        }
    }
}
