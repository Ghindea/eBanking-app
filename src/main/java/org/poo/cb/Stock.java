package org.poo.cb;

import java.util.LinkedList;

public class Stock {
    private String companyName;
    private LinkedList<Double> prices;

    public Stock(String companyName, LinkedList<Double> prices) {
        this.companyName = companyName;
        this.prices = prices;
    }

    public LinkedList<Double> getPrices() {
        return prices;
    }

    public String getCompanyName() {
        return companyName;
    }
}
