package org.poo.cb;

import org.poo.cb.exceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Bank {
    //region Singleton
    private static Bank instance = null;
    private Map<CurrencyEnum, Map<CurrencyEnum, Double>> exchangeRates;
    private List<Stock> stocks;
    private List<User> users;
    private Bank() {
        exchangeRates = new HashMap<>();
        stocks = new ArrayList<>();
        users = new ArrayList<>();
    }
    public static Bank getInstance() {
        if(instance == null) {
            instance = new Bank();
        }
        return instance;
    }
    //endregion
    //region Loaders
    public void loadExchangeRates(String filename) {
        try (Scanner in = new Scanner(new File(filename))) {
            String[] currencies = in.nextLine().split(",");
            currencies = Arrays.copyOfRange(currencies, 1, currencies.length); // remove "Base" element

            for(String currency : currencies) {
                String[] rates = in.nextLine().split(",");
                rates = Arrays.copyOfRange(rates, 1, rates.length);  // remove currency element

                HashMap<CurrencyEnum, Double> hashRates = new HashMap<>();
                for(int i = 0; i < currencies.length; i++) {
                    hashRates.put(CurrencyEnum.valueOf(currencies[i]), Double.parseDouble(rates[i]));
                }
                exchangeRates.put(CurrencyEnum.valueOf(currency), hashRates);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename + " not found");
        }
    }
    public void loadStockValues(String filename) {
        try (Scanner in = new Scanner(new File(filename))) {
            in.nextLine();
            while(in.hasNextLine()) {
                String[] stock = in.nextLine().split(",");
                LinkedList<Double> prices = new LinkedList<>();
                for(int i = 1; i < stock.length; i++) {
                    prices.add(Double.parseDouble(stock[i]));
                }
                stocks.add(new Stock(stock[0], prices));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename + " not found");
        }
    }
    //endregion
    //region Commands
    public void buyStock(String[] command) throws InsufficientAmountException {
        User u = findUser(command[2]);
        Stock s = findStock(command[3]);
        int noStocks = Integer.parseInt(command[4]);

        if(s.getPrices().getLast() * noStocks > u.getAccount(CurrencyEnum.USD).getBalance())
            throw new InsufficientAmountException(CurrencyEnum.USD.toString(), 1.0);
        u.getAccount(CurrencyEnum.USD).takeMoney(s.getPrices().getLast() * noStocks);
        u.addStock(s, noStocks);
    }
    public void makeAScam() {
        String s = "{\"stocksToBuy\":[ ";
        ArrayList<String> stocksToBuy = new ArrayList<>();
        for (Stock stock : stocks) {
            if (sma(stock) > 0) {
                stocksToBuy.add(stock.getCompanyName());
            }
        }
        for (int i = 0; i < stocksToBuy.size()-1; i++) {
            s += "\"" + stocksToBuy.get(i) + "\", ";
        }
        s += "\"" + stocksToBuy.get(stocksToBuy.size()-1) + "\"]}";
        System.out.println(s);
    }
    public void transfer(String[] command) throws InsufficientAmountException, UserIsNotFriendException {
        User user = findUser(command[2]), friend = findUser(command[3]);
        CurrencyEnum currency = CurrencyEnum.valueOf(command[4]);
        Double amount = Double.parseDouble(command[5]);
        if (user.isFriendWith(friend.getEmail())) {
            if (user.getAccount(currency).getBalance() > amount) {
                user.getAccount(currency).takeMoney(amount);
                friend.getAccount(currency).addMoney(amount);
                friend.getAccount(currency).notify("You received " + amount + " " + currency.toString() + " from " + user.getEmail());
            } else {
                throw new InsufficientAmountException(currency.toString(), 1);
            }
        } else {
            throw new UserIsNotFriendException(friend.getEmail());
        }
    }
    public void exchange(String[] command) throws InsufficientAmountException{
        User u = findUser(command[2]);
        CurrencyEnum src = CurrencyEnum.valueOf(command[3]), dest = CurrencyEnum.valueOf(command[4]);
        Double amount = Double.parseDouble(command[5]);
        Double exch = amount * exchangeRates.get(dest).get(src);

        if (exch > u.getAccount(src).getBalance()/2) {
            if (exch + exch * 0.01 > u.getAccount(src).getBalance())
                throw new InsufficientAmountException(src.toString(), true);
            else
                u.getAccount(src).takeMoney(exch + exch * 0.01);
        } else {
            u.getAccount(src).takeMoney(exch);
        }
        u.getAccount(dest).addMoney(amount);
    }
    public void addNewUser(String[] command) throws UserAlreadyExistsException {
        String email = command[2], firstName = command[3], lastName = command[4];
        String address = "";
        for(int i = 5; i < command.length - 1; i++) {
            address += command[i] + " ";
        }
        address += command[command.length - 1];
        User newUser = new User.UserBuilder()
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAddress(address)
                .build();
        if (findUser(email) == null)
            users.add(newUser);
        else
            throw new UserAlreadyExistsException(email);
    }
    public void addFriend(String[] command) throws UserNotFoundException{
        // ? check questions from @listUser
        AtomicReference<Boolean> ok1 = new AtomicReference<>(false); // check if user exists
        AtomicReference<Boolean> ok2 = new AtomicReference<>(false); // check if friend exists
        users.forEach(user -> {
            if(user.getEmail().equals(command[2])) {
               User friend = findUser(command[3]);
               if (friend != null) {
                   user.addFriend(friend);
                   friend.addFriend(user);
                   ok2.set(true);
               }
               ok1.set(true);
            }
        });
        if(!ok1.get()) throw new UserNotFoundException(command[2]);
        if(!ok2.get()) throw new UserNotFoundException(command[3]);
    }
    public void addAccount(String[] command) throws AccountAlreadyExitsException, InvalidCurrencyException {
        User u = findUser(command[2]);
        try {
            BankAccount a = new BankAccount(CurrencyEnum.valueOf(command[3]));
            for (BankAccount userAccount : u.getAccounts()) {
                if (userAccount.getCurrency().compareTo(a.getCurrency()) == 0)
                    throw new AccountAlreadyExitsException(userAccount.getCurrency().toString());
            }
            a.addObserver(u);
            u.addAccount(a);
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyException();
        }
    }
    public void addMoney(String[] command) throws InvalidCurrencyException {
        User u = findUser(command[2]);
        try {
            u.addDinero(CurrencyEnum.valueOf(command[3]), Double.parseDouble(command[4]));
            u.getAccount(CurrencyEnum.valueOf(command[3])).notify(command[4] + " " + command[3] + " were added to your account");
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyException();
        }
    }
    //endregion
    //region Getters
    public Map<CurrencyEnum, Map<CurrencyEnum, Double>> getExchangeRates() {
        return this.exchangeRates;
    }
    public List<Stock> getStocks() {
        return this.stocks;
    }
    //endregion
    //region Extra
    public User findUser(String email) {
        for (User user : users) {
            if(user.getEmail().equals(email)) return user;
        }
        return null;
    }
    public Stock findStock(String companyName) {
        for (Stock stock : stocks) {
            if(stock.getCompanyName().equals(companyName)) return stock;
        }
        return null;
    }
    public Double sma(Stock stock) {
        Double fiveAVG = 0.0, tenAVG = 0.0;
        for (int i = 0; i < stock.getPrices().size(); i++) {
            tenAVG += stock.getPrices().get(i);
            if (i > 4)
                fiveAVG += stock.getPrices().get(i);
        }
        fiveAVG /= 5.0;
        tenAVG /= 10.0;

        return fiveAVG - tenAVG;
    }
    public static void cleanup() {
        getInstance().exchangeRates.clear();
        getInstance().stocks.clear();
        getInstance().users.clear();
        instance = null;
    }
    //endregion
}
