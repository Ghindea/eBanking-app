package org.poo.cb.exceptions;

public class AccountAlreadyExitsException extends Exception{
    public AccountAlreadyExitsException(String m) {super("Account in currency " + m + " already exists for user");}
}
