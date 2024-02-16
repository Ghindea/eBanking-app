package org.poo.cb.Strategy;

import org.poo.cb.User;

public class ListPortofolio implements ListPropertiesStrategy{
    @Override
    public void listProperties(User user) {
        System.out.println(user.portofolioToString());
    }
}
