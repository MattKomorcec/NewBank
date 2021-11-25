package newbank.server;

import java.util.ArrayList;

public class AccountManagement {

    Account selectedAccount;

    public String showMyAccounts(Customer customer) {
        return customer.accountsToString();
    }

    public String newAccount(Customer customer, NewBankClientHandler newBankClientHandler) {

        boolean valid = true;
        String newAccountType = "";
        ArrayList<Account> existAccounts;
        newBankClientHandler.sendOutput("Selected: Create new account");

        // Checking if the user has already reached the maximum number (3) of accounts.
        existAccounts = customer.getAccounts();
        if (existAccounts.size() >= 3) {
            newBankClientHandler.sendOutput("Maximum number of accounts reached. Exiting to Customer Menu.");
            return showMyAccounts(customer);
        }
        while (valid) {
            // Asking for account type.
            newBankClientHandler.sendOutput("Please select a new account type [MAIN, SAVINGS, INVESTMENTS] or 'Q' to quit:");
            newAccountType = newBankClientHandler.getInput();
            // Checking if  user input is "q".
            if (checkQuitInput(newAccountType)) {
                return "Customer Menu";
            }
            // Checking if user input is blank.
            if (newAccountType.trim().length() == 0 || newAccountType.length() == 0) {
                newBankClientHandler.sendOutput("An appropriate account type  must be selected [MAIN, SAVINGS, INVESTMENTS]. Please try again:");
                continue;
            }
            // Checking if the selected account type already exists.
            if (!customer.checkExistingAccount(newAccountType)) {
                Account.AccountType accountType;
                if (newAccountType.equalsIgnoreCase(Account.AccountType.MAIN.toString())) {
                    accountType = Account.AccountType.MAIN;
                    customer.addAccount(new Account(accountType, 0.0));
                    valid = false;
                }
                else if (newAccountType.equalsIgnoreCase(Account.AccountType.SAVINGS.toString())) {
                    accountType = Account.AccountType.SAVINGS;
                    customer.addAccount(new Account(accountType, 0.0));
                    valid = false;
                }
                else if (newAccountType.equalsIgnoreCase(Account.AccountType.INVESTMENTS.toString())) {
                    accountType = Account.AccountType.INVESTMENTS;
                    customer.addAccount(new Account(accountType, 0.0));
                    valid = false;
                }
                else {
                    newBankClientHandler.sendOutput("An appropriate account type  must be selected [MAIN, SAVINGS, INVESTMENTS]. Please try again:");
                }
            }
            else {
                newBankClientHandler.sendOutput("The selected account type already exists. Please try again:");
            }
        }
        return "New account was successfully created. Exiting to Customer Menu.";
    }

    public String removeAccount (Customer customer, NewBankClientHandler newBankClientHandler) {

        ArrayList<Account> existAccounts = customer.getAccounts();
        Transaction transaction = new Transaction();
        Account selectedAccount;

        newBankClientHandler.sendOutput("Selected: Remove existing account");
        // Checking if the customer has more than one account types. Minimum one account per customer is needed.
        if (existAccounts.size() < 2){
            return "You have only one account under your name. Request cannot be processed any further. Exiting to Customer Menu.";
        }
        else {
            newBankClientHandler.sendOutput("Please enter name of your account you wish to remove or 'Q' to quit:");
            // Checking if the selected account exist in the Account list.
            selectedAccount = transaction.getMyAccount(customer, newBankClientHandler);
            // Checking if the user wishes to quit.
            if (selectedAccount == null) {
                return "Customer Menu";
            }
            // Checking if the balance of the selected account is 0.0.
            if (selectedAccount.getBalance() != 0) {
                return "The selected account cannot be deleted as the outstanding balance is not 0.00. Exiting to Customer Menu.\n";
            }
            else {
                customer.removeAccount(selectedAccount);
            }
        }

        newBankClientHandler.sendOutput("The " + selectedAccount.getAccountType().toString()+  " account is successfully removed.\n");
        newBankClientHandler.sendOutput("New accounts' statement:");
        return customer.accountsToString();
    }

    private boolean checkQuitInput(String s) {
        return s.equalsIgnoreCase("Q");
    }
}
