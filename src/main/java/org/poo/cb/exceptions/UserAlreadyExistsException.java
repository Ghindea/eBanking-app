package org.poo.cb.exceptions;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(String m) {
        super("User with " + m + " already exists");
    }
}
