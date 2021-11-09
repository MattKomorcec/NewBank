package newbank.server;

public class AccManagement{

    public AccManagement() {
    }

    //showAccounts
    public String showMyAccounts(CustomerID customer) {
        return (NewBank.getBank().getCustomers().get(customer.getKey())).accountsToString();
    }
    //newAccount
    public String newAccount(CustomerID customer, NewBankClientHandler newBankClientHandler){

        boolean valid = false;
        String newAccountName = "";

        while (valid == false) {

            //ask for account name
            newBankClientHandler.sendOutput("Please enter new account name:");
            newAccountName = newBankClientHandler.getInput();

            //if account name is blank, loop
            if (newAccountName.trim().length()==0 || newAccountName.length() ==0){
                newBankClientHandler.sendOutput("Account name must not be blank, please try again.");
                continue;
            }
            //if account name already exists, prompt new input, else valid is true and exit while loop
            if (!NewBank.getBank().getCustomers().get(customer.getKey()).checkExistingAccount(newAccountName)){
                valid = true;
            }else{
                newBankClientHandler.sendOutput("Account name is taken, please try again.");
            }
        }
        //creates new account, defaults to balance of 0.0
        NewBank.getBank().getCustomers().get(customer.getKey()).addAccount(new Account(newAccountName, 0.0));
        return "SUCCESS";
    }

}
