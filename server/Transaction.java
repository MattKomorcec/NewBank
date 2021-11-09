package newbank.server;

public class Transaction {

    String input = "";
    double amount = 0.0;
    boolean valid = false;
    Account fromAccount;
    Account toAccount;
    Customer toCustomer;

    //move funds
    public String moveFunds(CustomerID customer, NewBankClientHandler newBankClientHandler){

        //gets user to input amount to transfer
        amount = getAmount(newBankClientHandler);
        //gets user to input account to transfer from
        newBankClientHandler.sendOutput("Please enter name of account to transfer funds from:");
        fromAccount = getMyAccount(customer, newBankClientHandler);
        //gets user to input account to transfer to
        newBankClientHandler.sendOutput("Please enter name of account to transfer funds to:");
        toAccount = getMyAccount(customer, newBankClientHandler);

        //checks if funds not sufficient, if so then exits
        if (!checkSufficientFunds(amount, fromAccount)) {
            newBankClientHandler.sendOutput("Insufficient funds, exiting to main menu");
            return "FAIL";
        }

        //subtracts amount from the 'from' account
        double fromBalance = fromAccount.getOpeningBalance();
        fromAccount.setOpeningBalance(fromBalance - amount);
        //add amount to the 'to' account
        double toBalance = toAccount.getOpeningBalance();
        toAccount.setOpeningBalance(toBalance + amount);

        //prints accounts and balance
        newBankClientHandler.sendOutput((NewBank.getBank().getCustomers().get(customer.getKey())).accountsToString());

        return "SUCCESS";
    }

    public String payFunds(CustomerID customer, NewBankClientHandler newBankClientHandler){
        //gets user to input transfer amount
        amount = getAmount(newBankClientHandler);

        //gets user to input account to transfer from
        newBankClientHandler.sendOutput("Please enter name of your account to transfer funds from:");
        fromAccount = getMyAccount(customer, newBankClientHandler);
        //gets user to input customer to transfer to
        newBankClientHandler.sendOutput("Please enter name of payee:");
        toCustomer = getToCustomer(customer, newBankClientHandler);
        //gets user to input customer account
        newBankClientHandler.sendOutput("Please enter name of payee's account:");
        toAccount = getToCustomerAccount(newBankClientHandler);

        //checks if funds not sufficient, if so then exits
        if (!checkSufficientFunds(amount, fromAccount)) {
            newBankClientHandler.sendOutput("Insufficient funds, exiting to main menu");
            return "FAIL";
        }

        //subtracts amount from the 'from' account
        double fromBalance = fromAccount.getOpeningBalance();
        fromAccount.setOpeningBalance(fromBalance - amount);
        //add amount to the 'to' account
        double toBalance = toAccount.getOpeningBalance();
        toAccount.setOpeningBalance(toBalance + amount);

        newBankClientHandler.sendOutput("New account balance:");
        newBankClientHandler.sendOutput((NewBank.getBank().getCustomers().get(customer.getKey())).accountsToString());

        return "SUCCESS";
    }

    private double getAmount(NewBankClientHandler newBankClientHandler){
        valid = false;

        while (valid == false) {
            //asks for amount
            newBankClientHandler.sendOutput("Please enter amount to be transferred:");
            input = newBankClientHandler.getInput();
            //checks if type double
            if (checkDouble(input)){
                //checks if negative
                if (checkNegative(Double.parseDouble(input))){
                    newBankClientHandler.sendOutput("Invalid negative input, please try again:");
                }else {
                    return Double.parseDouble(input);
                }
            }else {
                newBankClientHandler.sendOutput("Invalid input, please try again:");
            }
        }
        return 0.0;
    }

    private Account getMyAccount(CustomerID customer, NewBankClientHandler newBankClientHandler){
        String input;
        Account account;
        valid = false;
        /* asks for account name
           if the account exists, it return the account
           else invalid input and loops
         */
        while (valid == false) {
            input = newBankClientHandler.getInput();
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
        /* asks for customer name
           if the account exists, it returns the account
           else invalid input and loops
         */
        while (valid == false) {
            input = newBankClientHandler.getInput();
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
        /* asks for the payee's account name
           if the account exists, it return the account
           else invalid input and loops
         */
        while (valid == false) {
            input = newBankClientHandler.getInput();
            if (toCustomer.checkExistingAccount(input)){
                valid = true;
                return toCustomer.getExistingAccount(input);
            }else{
                newBankClientHandler.sendOutput("Invalid input, please try again:");
            }
        }
        return null;
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

    private boolean checkSufficientFunds(double amount, Account fromAccount) {
        return !((fromAccount.getOpeningBalance() - amount) < 0);
    }


    private boolean checkNegative(double d){
        return d < 0;
    }

}
