package org.poo.cb.Strategy;

import org.poo.cb.User;
import org.poo.cb.exceptions.UserNotFoundException;

public class ListProperty {
    private ListPropertiesStrategy strategy;
    public ListProperty() {}
    public void setStrategy(ListPropertiesStrategy strategy) {
        this.strategy = strategy;
    }
    public void listProperties(User u) {
        strategy.listProperties(u);
    }
}
