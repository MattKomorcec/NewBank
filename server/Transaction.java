package newbank.server;

public class Transaction {

    String input = "";
    String confirmation = "";
    double amount = 0.0;
    boolean valid = false;
    Account fromAccount;
    Account toAccount;
    Customer toCustomer;

    //move funds between personal accounts
    public String moveFunds(CustomerID customer, NewBankClientHandler newBankClientHandler){
        newBankClientHandler.sendOutput("Selected: Move funds between your accounts");
        //gets user to input amount to transfer
        amount = getAmount(newBankClientHandler);
        //if user quit
        if (amount == -1){return "MENU";}

        //gets user to input account to transfer from
        newBankClientHandler.sendOutput("Please enter name of account to transfer funds from ('q' to quit):");
        fromAccount = getMyAccount(customer, newBankClientHandler);
        //if user quit
        if (fromAccount == null){return "MENU";}

        //gets user to input account to transfer to
        newBankClientHandler.sendOutput("Please enter name of account to transfer funds to ('q' to quit):");
        toAccount = getMyAccount(customer, newBankClientHandler);
        //if user quit
        if (toAccount == null){return "MENU";}

        //checks if funds not sufficient, if so then exits
        if (!checkSufficientFunds(amount, fromAccount)) {
            newBankClientHandler.sendOutput("Insufficient funds, exiting to main menu");
            return "FAIL";
        }

        //confirm transaction
        confirmation = String.format("Transfer: £%.2f\nFrom: %s\nTo: %s",
                amount, fromAccount.getAccountName(), toAccount.getAccountName());
        newBankClientHandler.sendOutput(confirmation +"\nHit 'Y' to confirm, otherwise return to menu:");
        if (!getYN(newBankClientHandler)){return "MENU";}

        //completes transfer
        transferFunds(amount, fromAccount, toAccount);

        //prints accounts and balance
        newBankClientHandler.sendOutput((NewBank.getBank().getCustomers().get(customer.getKey())).accountsToString());

        return "SUCCESS";
    }

    public String payFunds(CustomerID customer, NewBankClientHandler newBankClientHandler){
        newBankClientHandler.sendOutput("Selected: Make payment");
        //gets user to input transfer amount
        amount = getAmount(newBankClientHandler);
        //if user quit
        if (amount == -1){return "MENU";}

        //gets user to input account to transfer from
        newBankClientHandler.sendOutput("Please enter name of your account to transfer funds from ('q' to quit):");
        fromAccount = getMyAccount(customer, newBankClientHandler);
        //if user quit
        if (fromAccount == null){return "MENU";}

        //gets user to input customer to transfer to
        newBankClientHandler.sendOutput("Please enter name of payee ('q' to quit):");
        toCustomer = getToCustomer(customer, newBankClientHandler);
        //if user quit
        if (toCustomer == null){return "MENU";}

        //gets user to input customer account
        newBankClientHandler.sendOutput("Please enter name of payee's account ('q' to quit):");
        toAccount = getToCustomerAccount(newBankClientHandler);
        //if user quit
        if (toAccount == null){return "MENU";}

        //checks if funds not sufficient, if so then exits
        if (!checkSufficientFunds(amount, fromAccount)) {
            newBankClientHandler.sendOutput("Insufficient funds, exiting to main menu");
            return "FAIL";
        }

        //confirm transaction
        confirmation = String.format("Transfer: £%.2f\nFrom: %s\nTo: %s %s",
                amount, fromAccount.getAccountName(),
                NewBank.getBank().getID(toCustomer), toAccount.getAccountName());
        newBankClientHandler.sendOutput(confirmation + "\nHit 'Y' to confirm, otherwise return to menu:");
        if (!getYN(newBankClientHandler)){return "MENU";}

        transferFunds(amount, fromAccount, toAccount);

        //prints the updated fromAccount balance
        newBankClientHandler.sendOutput("New account balance:");
        newBankClientHandler.sendOutput((NewBank.getBank().getCustomers().get(customer.getKey())).accountsToString());

        return "SUCCESS";
    }
    //gets user input for transfer
    private double getAmount(NewBankClientHandler newBankClientHandler){
        valid = false;

        while (valid == false) {
            //asks for amount
            newBankClientHandler.sendOutput("Please enter amount to be transferred ('q' to quit):");
            input = newBankClientHandler.getInput();
            //check if quit
            if (checkQuitInput(input)){return -1;}
            //checks if type double
            if (checkDouble(input)){
                //checks if negative
                if (checkNegative(Double.parseDouble(input))){
                    newBankClientHandler.sendOutput("Invalid negative input, please try again:");
                }else {
                    //if type double and non-negative, returns the input as type double
                    return Double.parseDouble(input);
                }
            }else {
                //invalid input, loops
                newBankClientHandler.sendOutput("Invalid input, please try again:");
            }
        }
        return 0.0;
    }

    private Account getMyAccount(CustomerID customer, NewBankClientHandler newBankClientHandler){
        String input;
        Account account;
        valid = false;

        while (valid == false) {
            //asks for account name
            input = newBankClientHandler.getInput();
            //check if quit
            if (checkQuitInput(input)) {return null;}

            //checks if account exists, if it does then returns the Account Object
            //Otherwise, error message and loops
            if (NewBank.getBank().getCustomers().get(customer.getKey()).checkExistingAccount(input)){
                valid = true;
                return NewBank.getBank().getCustomers().get(customer.getKey()).getExistingAccount(input);
            }else{
                newBankClientHandler.sendOutput("Invalid input, please try again:");
            }
        }
        return null;
    }

    private Customer getToCustomer(CustomerID customer, NewBankClientHandler newBankClientHandler){
        String input;
        Customer payee;
        valid = false;
        while (valid == false) {
            //asks for customer name
            input = newBankClientHandler.getInput();
            //check if quit
            if (checkQuitInput(input)) {return null;}

            //checks if customer exists, if does returns Customer Object
            //else loops
            if (NewBank.getBank().getCustomers().containsKey(input)){
                valid = true;
                payee = NewBank.getBank().getCustomers().get(input);
                return payee;
            }else{
                newBankClientHandler.sendOutput("No customer found, please try again:");
            }
        }
        return null;
    }

    private Account getToCustomerAccount(NewBankClientHandler newBankClientHandler){
        String input;
        Account account;
        valid = false;

        while (valid == false) {
            //asks for payee account input
            input = newBankClientHandler.getInput();
            //check if quit
            if (checkQuitInput(input)) {return null;}

            //if account exists, returns the Account Object
            //else error message and loops
            if (toCustomer.checkExistingAccount(input)){
                valid = true;
                return toCustomer.getExistingAccount(input);
            }else{
                newBankClientHandler.sendOutput("Invalid input, please try again:");
            }
        }
        return null;
    }

    //
    public void transferFunds(double amount, Account fromAccount, Account toAccount){
        //subtracts amount from the 'from' account
        double fromBalance = fromAccount.getOpeningBalance();
        fromAccount.setOpeningBalance(fromBalance - amount);
        //add amount to the 'to' account
        double toBalance = toAccount.getOpeningBalance();
        toAccount.setOpeningBalance(toBalance + amount);
    }

    private boolean checkDouble(String s){

        //tests if input string is of double format
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    //if sufficient funds, return true else false
    private boolean checkSufficientFunds(double amount, Account fromAccount) {
        return !((fromAccount.getOpeningBalance() - amount) < 0);
    }

    //if argument is negative return true
    private boolean checkNegative(double d){
        return d < 0;
    }

    private boolean checkQuitInput(String s){
        return s.equalsIgnoreCase("q");
    }

    private boolean getYN(NewBankClientHandler newBankClientHandler){
        //asks for input
        input = newBankClientHandler.getInput();
        return input.equalsIgnoreCase("y");
    }
}
