package org.poo.cb;

import org.poo.cb.Strategy.ListNotitfications;
import org.poo.cb.Strategy.ListPortofolio;
import org.poo.cb.Strategy.ListProperty;
import org.poo.cb.Strategy.ListUser;
import org.poo.cb.exceptions.UserNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    private static final String antetResources = "src/main/resources/";

    public static void main(String[] args) {
        /*
         * args[0] = exchange rate file
         * args[1] = stock values file
         * args[2] = commands file
         */

        if(args == null) {System.out.println("Running Main"); return;}

        Bank bank = Bank.getInstance();
        bank.loadExchangeRates(antetResources + args[0]);
        bank.loadStockValues(  antetResources + args[1]);

        try (Scanner in = new Scanner(new File(antetResources + args[2]))) {
            while (in.hasNextLine()) {
                String[] command = in.nextLine().split(" ");
                switch (command[0]) {
                    case "CREATE": {
                        bank.addNewUser(command);
                        break;
                    }
                    case "LIST": {
                        ListProperty printer = new ListProperty();
                        switch (command[1]) {
                            case "USER" : {
                                printer.setStrategy(new ListUser());
                                break;
                            }
                            case "PORTFOLIO" : {
                                printer.setStrategy(new ListPortofolio());
                                break;
                            }
                            case "NOTIFICATIONS" : {
                                printer.setStrategy(new ListNotitfications());
                                break;
                            }
                        }
                        if (bank.findUser(command[2]) == null) throw new UserNotFoundException(command[2]);
                        printer.listProperties(bank.findUser(command[2]));
                        break;
                    }
                    case "ADD": {
                        switch (command[1]) {
                            case "FRIEND" : {
                                bank.addFriend(command);
                                break;
                            }
                            case "ACCOUNT" : {
                                bank.addAccount(command);
                                break;
                            }
                            case "MONEY" : {
                                bank.addMoney(command);
                                break;
                            }
                        }
                        break;
                    }
                    case "EXCHANGE" : {
                        bank.exchange(command);
                        break;
                    }
                    case "TRANSFER" : {
                        bank.transfer(command);
                        break;
                    }
                    case "RECOMMEND" : {
                        bank.makeAScam();
                        break;
                    }
                    case "BUY" : {
                        bank.buyStock(command);
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + args[2] + " not found");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            Bank.cleanup();
        }
    }
}