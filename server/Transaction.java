package newbank.server;

public class Transaction {

    String input = "";
    String confirmation = "";
    double amount = 0.0;
    boolean valid = false;
    newbank.server.Account fromAccount;
    newbank.server.Account toAccount;
    newbank.server.Customer toCustomer;

    // Moving funds between personal accounts.
    public String moveFunds(newbank.server.Customer customer, newbank.server.NewBankClientHandler newBankClientHandler) {
        newBankClientHandler.sendOutput("Selected: Move funds between your accounts");
        // gets user to input amount to transfer
        amount = getAmount(newBankClientHandler);
        //if user quit
        if (amount == -1) {
            return "MENU";
        }
        // Getting user to input account to transfer from.
        newBankClientHandler.sendOutput("Please enter name of account to transfer funds from ('q' to quit):");
        fromAccount = getMyAccount(customer, newBankClientHandler);
        // Checking if user quit.
        if (fromAccount == null) {
            return "MENU";
        }
        // Getting user input account to transfer to.
        newBankClientHandler.sendOutput("Please enter name of account to transfer funds to ('q' to quit):");
        toAccount = getMyAccount(customer, newBankClientHandler);
        //if user quit
        if (toAccount == null) {
            return "MENU";
        }
        // Checks if funds not sufficient, if so then exits
        if (!checkSufficientFunds(amount, fromAccount)) {
            newBankClientHandler.sendOutput("Insufficient funds, exiting to main menu");
            return "FAIL";
        }
        // Confirming transaction.
        confirmation = String.format("Transfer: £%.2f\nFrom: %s\nTo: %s",
                amount, fromAccount.getAccountName(), toAccount.getAccountName());
        newBankClientHandler.sendOutput(confirmation + "\nHit 'Y' to confirm, otherwise return to menu:");
        if (!getYN(newBankClientHandler)) {
            return "MENU";
        }
        // Completing transfer.
        transferFunds(amount, fromAccount, toAccount);
        // Printing accounts and balance.
        newBankClientHandler.sendOutput(customer.accountsToString());
        return "SUCCESS";
    }

    public String payFunds(newbank.server.Customer customer, newbank.server.NewBankClientHandler newBankClientHandler) {
        newBankClientHandler.sendOutput("Selected: Make payment");
        // Getting user's input transfer amount.
        amount = getAmount(newBankClientHandler);
        // Checking if user quits
        if (amount == -1) {
            return "MENU";
        }
        // Getting user's input account to transfer from.
        newBankClientHandler.sendOutput("Please enter name of your account to transfer funds from ('q' to quit):");
        fromAccount = getMyAccount(customer, newBankClientHandler);
        //if user quit
        if (fromAccount == null) {
            return "MENU";
        }
        // Getting user's input customer to transfer to.
        newBankClientHandler.sendOutput("Please enter name of payee ('q' to quit):");
        toCustomer = getToCustomer(customer, newBankClientHandler);
        // Checking if user quits
        if (toCustomer == null) {
            return "MENU";
        }
        // Getting user's to input customer account.
        newBankClientHandler.sendOutput("Please enter name of payee's account ('q' to quit):");
        toAccount = getToCustomerAccount(newBankClientHandler);
        //Checking if user quits.
        if (toAccount == null) {
            return "MENU";
        }
        // Checking if funds are sufficient - if so then exits.
        if (!checkSufficientFunds(amount, fromAccount)) {
            newBankClientHandler.sendOutput("Insufficient funds, exiting to main menu");
            return "FAIL";
        }
        // Confirming transaction.
        confirmation = String.format("Transfer: £%.2f\nFrom: %s\nTo: %s %s",
                amount, fromAccount.getAccountName(),
                newbank.server.NewBank.getBank().getID(toCustomer), toAccount.getAccountName());
        newBankClientHandler.sendOutput(confirmation + "\nHit 'Y' to confirm, otherwise return to menu:");
        if (!getYN(newBankClientHandler)) {
            return "MENU";
        }
        transferFunds(amount, fromAccount, toAccount);
        // Printing the updated fromAccount balance.
        newBankClientHandler.sendOutput("New account balance:");
        newBankClientHandler.sendOutput(customer.accountsToString());
        return "SUCCESS";
    }

    // Getting user input for transfer.
    private double getAmount(newbank.server.NewBankClientHandler newBankClientHandler) {
        valid = false;
        while (!valid) {
            // Asking for amount.
            newBankClientHandler.sendOutput("Please enter amount to be transferred ('q' to quit):");
            input = newBankClientHandler.getInput();
            // Checking if quit.
            if (checkQuitInput(input)) {
                return -1;
            }
            // Checking if type double.
            if (checkDouble(input)) {
                // Checking if negative.
                if (checkNegative(Double.parseDouble(input))) {
                    newBankClientHandler.sendOutput("Invalid negative input, please try again:");
                } else {
                    // Returning the input as type double if type double and non-negative.
                    return Double.parseDouble(input);
                }
            } else {
                // Looping for invalid input.
                newBankClientHandler.sendOutput("Invalid input, please try again:");
            }
        }
        return 0.0;
    }

    private newbank.server.Account getMyAccount(newbank.server.Customer customer, newbank.server.NewBankClientHandler newBankClientHandler) {
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
            }
            else {
                newBankClientHandler.sendOutput("Invalid input, please try again:");
            }
        }
        return null;
    }

    private newbank.server.Customer getToCustomer(newbank.server.Customer customer, newbank.server.NewBankClientHandler newBankClientHandler) {
        String input;
        newbank.server.Customer payee;
        valid = false;
        while (!valid) {
            // Asking for customer name.
            input = newBankClientHandler.getInput();
            // Checking if quit.
            if (checkQuitInput(input)) {
                return null;
            }
            // Checking if customer exists - if so returning Customer Object
            // Else loops.
            if (newbank.server.NewBank.getBank().getCustomers().containsKey(input)) {
                valid = true;
                payee = newbank.server.NewBank.getBank().getCustomers().get(input);
                return payee;
            } else {
                newBankClientHandler.sendOutput("No customer found, please try again:");
            }
        }
        return null;
    }

    private newbank.server.Account getToCustomerAccount(newbank.server.NewBankClientHandler newBankClientHandler) {
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
            }
            else {
                newBankClientHandler.sendOutput("Invalid input, please try again:");
            }
        }
        return null;
    }

    public void transferFunds(double amount, newbank.server.Account fromAccount, newbank.server.Account toAccount) {
        // Subtracting amount from the 'from' account.
        double fromBalance = fromAccount.getBalance();
        fromAccount.setBalance(fromBalance - amount);
        // Adding amount to the 'to' account.
        double toBalance = toAccount.getBalance();
        toAccount.setBalance(toBalance + amount);
    }

    private boolean checkDouble(String s) {
        // Checking if input string is of double format.
        try {
            Double.parseDouble(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // Returning true if there are sufficient funds, else false.
    private boolean checkSufficientFunds(double amount, newbank.server.Account fromAccount) {
        return !((fromAccount.getBalance() - amount) < 0);
    }

    // Returning true if argument is negative.
    private boolean checkNegative(double d) {
        return d < 0;
    }

    // Checking if user inputs 'q'.
    private boolean checkQuitInput(String s) {
        return s.equalsIgnoreCase("q");
    }

    // Getting input - if 'Y' or 'y' returns true, otherwise returns false
    private boolean getYN(newbank.server.NewBankClientHandler newBankClientHandler) {
        //asks for input
        input = newBankClientHandler.getInput();
        return input.equalsIgnoreCase("y");
    }
}
