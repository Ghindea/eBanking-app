package org.poo.cb.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String m) {
        super("user with email " + m + " doesn't exist");
    }
}
