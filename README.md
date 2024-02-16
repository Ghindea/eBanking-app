# eBanking App
Homework realised for OOP course, second year, FACC-CSIT

made by: [Ghindea Daniel-Teodor](https://github.com/Ghindea)

[requirements](./Proiect%20POO%202024.pdf)

---

### Synopsis:

This program consists of five main classes that define the properties of the used objects and their functionalities:

- `Main` - the main class that contains methods for loading the data from the input files and for running the program
- `Bank` - **singleton** class 
  - contains the list of all the users and the list of all the stocks in the system.
  - contains a hashmap of all exchange rates
  - contains methods for all functionalities of the bank
- `User` - **observer** class
  - class that contains the properties of a user and the methods for the functionalities of the user
  - contains a list of all the notifications of the user that are received when money are deposited to an account or when a friend transfers money to the user
  - contains an inner class `UserBuilder` that is used to **build** a user object
- `Stock` - **subject** class
  - class that contains the properties of a stock and the methods for the functionalities of the stock
- `ListPropery` - **strategy** class
    - used to implement the strategy pattern for the listing of different types of properties of a user (details, portfolio, notifications)

### Chosen designs patterns:
- [x] Singleton
- [x] Strategy
- [x] Builder
- [x] Observer

### Extra:
I have implemented this test to check Observer DP:
```java
public void test9() throws IOException {
        ByteArrayOutputStream outPrintStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outPrintStream));
        String commandFolder = "test9/";

        Main.main(getInputArgs(commandFolder));
        String output = outPrintStream.toString();

        assertJsonLineAreEqual(output, commandFolder);

        System.setOut(System.out);
    }
```
with the following input:
```
CREATE USER bob.stucky@email.com Bob Stucky 687 Swanson Dr. New York NY 10024
LIST USER bob.stucky@email.com
ADD ACCOUNT bob.stucky@email.com USD
ADD MONEY bob.stucky@email.com USD 50000
CREATE USER daniel.ghindea@gmail.com Daniel Ghindea Str.Politemnistului 1 Bucuresti Romania
ADD ACCOUNT daniel.ghindea@gmail.com USD
ADD MONEY daniel.ghindea@gmail.com USD 100000
ADD FRIEND daniel.ghindea@gmail.com bob.stucky@email.com
TRANSFER MONEY bob.stucky@email.com daniel.ghindea@gmail.com USD 5000
LIST USER daniel.ghindea@gmail.com
LIST NOTIFICATIONS daniel.ghindea@gmail.com
```

### Encountered problems:
Before I came up with the idea of implementing Strategy DP I have encountered this situation:
```java
public void listUser(String[] command) throws UserNotFoundException{
        /*
         * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
         * I thought that tests don't contain invalid users, and when I wanted to make sure InteliJ
         * suggested to use AtomicReference, so I did it, but that raised me questions about lambdas.
         *
         * -> how the process of using variables from outside the lambda expression works? (o_O)?
         * -> what is functional programming?
         * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
         */

        AtomicReference<Boolean> ok = new AtomicReference<>(false);
        users.forEach(user -> {
            if(user.getEmail().equals(command[2])) {
                System.out.println(user); ok.set(true);
            }
        });
        if(!ok.get()) throw new UserNotFoundException(command[2]);
    }
```