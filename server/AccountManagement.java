package newbank.server;

import java.util.ArrayList;

public class AccountManagement {

    public String showMyAccounts(Customer customer, NewBankClientHandler newBankClientHandler) {
        newBankClientHandler.sendOutput("Selected: Show existing accounts and balance\n");
        newBankClientHandler.sendOutput(customer.accountsToString());
        newBankClientHandler.sendOutput("\nExiting to Customer Menu.");
        return newBankClientHandler.printCustomerMenu();
    }

    public String newAccount(Customer customer, NewBankClientHandler newBankClientHandler) {

        boolean valid = false;
        String newAccountType;
        ArrayList<Account> existAccounts;
        newBankClientHandler.sendOutput("Selected: Create new account");

        // Checking if the user has already reached the maximum number (3) of accounts.
        existAccounts = customer.getAccounts();
        if (existAccounts.size() >= 3) {
            newBankClientHandler.sendOutput("Maximum number of accounts reached. Exiting to Customer Menu.");
            newBankClientHandler.sendOutput(customer.accountsToString());
            return newBankClientHandler.printCustomerMenu();
        }
        while (!valid) {
            // Asking for account type.
            newBankClientHandler.sendOutput("Please select a new account type [MAIN, SAVINGS, INVESTMENTS] or 'Q' to quit:");
            newAccountType = newBankClientHandler.getInput();
            // Checking if  user input is "q".
            if (checkQuitInput(newAccountType)) {
                newBankClientHandler.sendOutput("\nExiting to Customer Menu.");
                return newBankClientHandler.printCustomerMenu();
            }
            // Checking if user input is blank.
            if (newAccountType.trim().length() == 0 || newAccountType.length() == 0) {
                newBankClientHandler.sendOutput("An appropriate account type must be selected [MAIN, SAVINGS, INVESTMENTS]. Please try again:");
                continue;
            }
            // Checking if the selected account type already exists.
            if (!customer.checkExistingAccount(newAccountType)) {
                Account.AccountType accountType;
                if (newAccountType.equalsIgnoreCase(Account.AccountType.MAIN.toString())) {
                    accountType = Account.AccountType.MAIN;
                    customer.addAccount(new Account(null, accountType.toString(),0, null, customer.getID()));
                    valid = true;
                } else if (newAccountType.equalsIgnoreCase(Account.AccountType.SAVINGS.toString())) {
                    accountType = Account.AccountType.SAVINGS;
                    customer.addAccount(new Account(null, accountType.toString(),0, null, customer.getID()));
                    valid = true;
                } else if (newAccountType.equalsIgnoreCase(Account.AccountType.INVESTMENTS.toString())) {
                    accountType = Account.AccountType.INVESTMENTS;
                    customer.addAccount(new Account(null, accountType.toString(),0, null, customer.getID()));
                    valid = true;
                } else {
                    newBankClientHandler.sendOutput("An appropriate account type must be selected [MAIN, SAVINGS, INVESTMENTS]. Please try again:");
                }
            } else {
                newBankClientHandler.sendOutput("The selected account type already exists. Please try again:");
            }
        }
        newBankClientHandler.sendOutput("New account was successfully created. Exiting to Customer Menu.");
        return newBankClientHandler.printCustomerMenu();
    }

    public String removeAccount(Customer customer, NewBankClientHandler newBankClientHandler) {

        ArrayList<Account> existAccounts = customer.getAccounts();
        Transaction transaction = new Transaction();
        Account selectedAccount;

        newBankClientHandler.sendOutput("Selected: Remove existing account");
        // Checking if the customer has more than one account types. Minimum one account per customer is needed.
        if (existAccounts.size() < 2) {
            newBankClientHandler.sendOutput("You have only one account under your name. Request cannot be processed any further. Exiting to Customer Menu.");
            return newBankClientHandler.printCustomerMenu();
        } else {
            newBankClientHandler.sendOutput("Please enter the account type you wish to remove or 'Q' to quit:");
            // Checking if the selected account exist in the Account list.
            selectedAccount = transaction.getMyAccount(customer, newBankClientHandler);
            // Checking if the user wishes to quit.
            if (selectedAccount == null) {
                newBankClientHandler.sendOutput("\nExiting to Customer Menu.");
                return newBankClientHandler.printCustomerMenu();
            }
            // Checking if the balance of the selected account is 0.0.
            if (selectedAccount.getBalance() != 0) {
                newBankClientHandler.sendOutput("The selected account cannot be deleted as the outstanding balance is not 0.00. Exiting to Customer Menu.");
                return newBankClientHandler.printCustomerMenu();
            } else {
                customer.removeAccount(selectedAccount);
            }
        }
        newBankClientHandler.sendOutput("The " + selectedAccount.getAccountType().toString() + " account is successfully removed.\n");
        newBankClientHandler.sendOutput("New accounts' statement:\n");
        newBankClientHandler.sendOutput(customer.accountsToString());
        newBankClientHandler.sendOutput("\nExiting to Customer Menu.");
        return newBankClientHandler.printCustomerMenu();
    }

    private boolean checkQuitInput(String s) {
        return s.equalsIgnoreCase("Q");
    }
}
