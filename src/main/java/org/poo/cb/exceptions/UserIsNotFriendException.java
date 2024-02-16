package org.poo.cb.exceptions;

import org.poo.cb.User;

public class UserIsNotFriendException extends Exception {
    public UserIsNotFriendException(String m) {super("You are not allowed to transfer money to " + m);}
}
