package org.poo.cb.Strategy;

import org.poo.cb.User;

public class ListUser implements ListPropertiesStrategy {
    @Override
    public void listProperties(User user) {
        System.out.println(user);
    }
}
