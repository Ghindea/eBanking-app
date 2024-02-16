package org.poo.cb.Strategy;

import org.poo.cb.User;

public class ListNotitfications implements ListPropertiesStrategy{
    @Override
    public void listProperties(User user) {
        for (String notification : user.getNotifications()) {
            System.out.println(notification);
        }
    }
}
