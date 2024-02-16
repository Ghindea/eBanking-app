package org.poo.cb;

public interface Subject {
    public void addObserver(User u);
    public void removeObserver(User u);
    public void notify(String message);
}
