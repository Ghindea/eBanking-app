package org.poo.cb.exceptions;

public class InsufficientAmountException extends Exception{
    public InsufficientAmountException(String m, boolean type) {super("Insufficient amount in account " + m + " for exchange");}
    public InsufficientAmountException(String m, int type)     {super("Insufficient amount in account " + m + " for transfer");}
    public InsufficientAmountException(String m, double type)     {super("Insufficient amount in account for buying stock");}
}
