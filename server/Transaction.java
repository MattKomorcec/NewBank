package newbank.server;

import static java.lang.Integer.parseInt;

public class Transaction {

    String input = "";
    String confirmation = "";
    int amount = 0;
    boolean valid = false;
    Account fromAccount;
    Account toAccount;
    Customer toCustomer;
    Database db = new Database();

    // Moving funds between personal accounts.
    public String moveFunds(Customer customer, NewBankClientHandler newBankClientHandler) {
        newBankClientHandler.sendOutput("Selected: Move funds between your accounts");
        // gets user to input amount to transfer.
        amount = getAmount(newBankClientHandler);
        // if user quit.
        if (amount == -1) {
            newBankClientHandler.sendOutput("\nExiting to Customer Menu");
            return newBankClientHandler.printCustomerMenu();
        }
        // Getting user to input account to transfer from.
        newBankClientHandler.sendOutput("Please enter name of account to transfer funds from or 'Q' to quit:");
        fromAccount = getMyAccount(customer, newBankClientHandler);
        // Checking if user quit.
        if (fromAccount == null) {
            newBankClientHandler.sendOutput("\nExiting to Customer Menu");
            return newBankClientHandler.printCustomerMenu();
        }
        // Getting user input account to transfer to.
        newBankClientHandler.sendOutput("Please enter name of account to transfer funds to or 'Q' to quit:");
        toAccount = getMyAccount(customer, newBankClientHandler);
        //if user quit
        if (toAccount == null) {
            newBankClientHandler.sendOutput("\nExiting to Customer Menu");
            return newBankClientHandler.printCustomerMenu();
        }
        // Checks if funds not sufficient, if so then exits
        if (!checkSufficientFunds(amount, fromAccount)) {
            newBankClientHandler.sendOutput("Insufficient funds. Exiting to Customer Menu.");
            return newBankClientHandler.printCustomerMenu();
        }
        // Confirming transaction.
        confirmation = String.format("Transfer: £%.2d\nFrom: %s\nTo: %s",
                amount, fromAccount.getAccountType(), toAccount.getAccountType());
        newBankClientHandler.sendOutput(confirmation + "\nHit 'Y' to confirm, otherwise return to Customer Menu:");
        if (!getYN(newBankClientHandler)) {
            newBankClientHandler.sendOutput("\nExiting to Customer Menu");
            return newBankClientHandler.printCustomerMenu();
        }
        // Completing transfer.
        transferFunds(customer, amount, fromAccount, toAccount);
        // Printing accounts and balance.
        newBankClientHandler.sendOutput("Transfer was successful. New accounts' statement:");
        newBankClientHandler.sendOutput(customer.accountsToString());
        newBankClientHandler.sendOutput("\nExiting to Customer Menu");
        return newBankClientHandler.printCustomerMenu();
    }

    public String payFunds(Customer customer, NewBankClientHandler newBankClientHandler) {
        newBankClientHandler.sendOutput("Selected: Make payment");
        // Getting user's input transfer amount.
        amount = getAmount(newBankClientHandler);
        // Checking if user quits
        if (amount == -1) {
            newBankClientHandler.sendOutput("\nExiting to Customer Menu");
            return newBankClientHandler.printCustomerMenu();
        }
        // Getting user's input account to transfer from.
        newBankClientHandler.sendOutput("Please enter name of your account to transfer funds from or 'Q' to quit:");
        fromAccount = getMyAccount(customer, newBankClientHandler);
        //if user quit
        if (fromAccount == null) {
            newBankClientHandler.sendOutput("\nExiting to Customer Menu");
            return newBankClientHandler.printCustomerMenu();
        }
        // Getting user's input customer to transfer to.
        newBankClientHandler.sendOutput("Please enter payee username or 'Q' to quit:");
        toCustomer = getToCustomer(newBankClientHandler);
        // Checking if user quits
        if (toCustomer == null) {
            newBankClientHandler.sendOutput("\nExiting to Customer Menu");
            return newBankClientHandler.printCustomerMenu();
        }
        // Getting user's to input customer account.
        newBankClientHandler.sendOutput("Please enter name of payee's account or 'Q' to quit:");
        toAccount = getToCustomerAccount(newBankClientHandler);
        //Checking if user quits.
        if (toAccount == null) {
            newBankClientHandler.sendOutput("\nExiting to Customer Menu");
            return newBankClientHandler.printCustomerMenu();
        }
        // Checking if funds are sufficient - if so then exits.
        if (!checkSufficientFunds(amount, fromAccount)) {
            newBankClientHandler.sendOutput("Insufficient funds. Exiting to Customer Menu.");
            return newBankClientHandler.printCustomerMenu();
        }
        // Confirming transaction.
        confirmation = String.format("Transfer: £%.2d\nFrom: %s\nTo: %s %s",
                amount, fromAccount.getAccountType(),
                toCustomer.getUsername(), toAccount.getAccountType());
        newBankClientHandler.sendOutput(confirmation + "\nHit 'Y' to confirm, otherwise return to Customer Menu:");
        if (!getYN(newBankClientHandler)) {
            newBankClientHandler.sendOutput("\nExiting to Customer Menu");
            return newBankClientHandler.printCustomerMenu();
        }
        transferFunds(customer, amount, fromAccount, toAccount);
        // Printing the updated fromAccount balance.
        newBankClientHandler.sendOutput("Payment was successful. New accounts' statement:");
        newBankClientHandler.sendOutput(customer.accountsToString());
        newBankClientHandler.sendOutput("\nExiting to Customer Menu");
        return newBankClientHandler.printCustomerMenu();
    }

    // Getting user input for transfer.
    public int getAmount(NewBankClientHandler newBankClientHandler) {
        valid = false;
        while (!valid) {
            // Asking for amount.
            newBankClientHandler.sendOutput("Please enter amount to be transferred or 'Q' to quit:");
            input = newBankClientHandler.getInput();
            // Checking if quit.
            if (checkQuitInput(input)) {
                return -1;
            }
            // Checking if type double.
            if (checkDouble(input)) {
                // Checking if negative.
                if (checkNegative(parseInt(input))) {
                    newBankClientHandler.sendOutput("Invalid negative input. Please try again:");
                } else {
                    // Returning the input as type double if type double and non-negative.
                    return parseInt(input);
                }
            } else {
                // Looping for invalid input.
                newBankClientHandler.sendOutput("Invalid input. Please try again:");
            }
        }
        return 0;
    }

    public Account getMyAccount(Customer customer, NewBankClientHandler newBankClientHandler) {
        String input;
        valid = false;
        while (!valid) {
            // Asking for account name.
            input = newBankClientHandler.getInput();
            // Checking if quit.
            if (checkQuitInput(input)) {
                return null;
            }
            // Checking if account exists - if so returning the Account Object.
            // Otherwise, error message and loops.
            if (customer.checkExistingAccount(input)) {
                valid = true;
                return customer.getExistingAccount(input);
            } else {
                newBankClientHandler.sendOutput("Invalid input. Please try again:");
            }
        }
        return null;
    }

    private Customer getToCustomer(NewBankClientHandler newBankClientHandler) {
        String input;
        Customer payee;
        valid = false;
        while (!valid) {
            // Asking for customer username.
            input = newBankClientHandler.getInput();
            // Checking if quit.
            if (checkQuitInput(input)) {
                return null;
            }
            // Checking if customer exists - if so returning Customer Object
            // Else loops.
            if (NewBank.getBank().getCustomer(input) != null) {
                valid = true;
                payee = NewBank.getBank().getCustomer(input);
                return payee;
            } else {
                newBankClientHandler.sendOutput("No customer found. Please try again:");
            }
        }
        return null;
    }

    private Account getToCustomerAccount(NewBankClientHandler newBankClientHandler) {
        String input;
        valid = false;
        while (!valid) {
            // Asking for payee account input.
            input = newBankClientHandler.getInput();
            // Checking if quit.
            if (checkQuitInput(input)) {
                return null;
            }
            // if account exists, returns the Account Object.
            // Else error message and loops.
            if (toCustomer.checkExistingAccount(input)) {
                valid = true;
                return toCustomer.getExistingAccount(input);
            } else {
                newBankClientHandler.sendOutput("Invalid input. Please try again:");
            }
        }
        return null;
    }

    public void transferFunds(Customer customer, int amount, Account fromAccount, Account toAccount) {
        // Subtracting amount from the 'from' account.
        int fromBalance = fromAccount.getBalance();
        fromAccount.setBalance(fromBalance - amount);

        // Adding amount to the 'to' account.
        int toBalance = toAccount.getBalance();
        toAccount.setBalance(toBalance + amount);

        //update database with above changes.
        try {
            int userID = db.getUserID(customer.getUsername());
            db.updateBalance(userID, fromAccount.getAccountType().toString(),fromBalance - amount);
            db.updateBalance(userID, toAccount.getAccountType().toString(), toBalance + amount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkDouble(String s) {
        // Checking if input string is of double format.
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // Returning true if there are sufficient funds, else false.
    private boolean checkSufficientFunds(int amount, Account fromAccount) {
        return !((fromAccount.getBalance() - amount) < 0);
    }

    // Returning true if argument is negative.
    private boolean checkNegative(int d) {
        return d < 0;
    }

    // Checking if user inputs 'q'.
    private boolean checkQuitInput(String s) {
        return s.equalsIgnoreCase("q");
    }

    // Getting input - if 'Y' or 'y' returns true, otherwise returns false
    private boolean getYN(NewBankClientHandler newBankClientHandler) {
        //asks for input
        input = newBankClientHandler.getInput();
        return input.equalsIgnoreCase("y");
    }
}