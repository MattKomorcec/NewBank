package newbank.server;

public class AccountManagement {

    public String showMyAccounts(Customer customer) {
        return customer.accountsToString();
    }
    public String newAccount(Customer customer, NewBankClientHandler newBankClientHandler){

        boolean valid = false;
        String newAccountName = "";
        newBankClientHandler.sendOutput("Selected: Create new account");

        while (!valid) {

            //ask for account name
            newBankClientHandler.sendOutput("Please enter new account name ('q' to quit):");
            newAccountName = newBankClientHandler.getInput();

            //if user quits
            if (checkQuitInput(newAccountName)){
                return "MENU";
            }

            //if account name is blank, loop
            if (newAccountName.trim().length()==0 || newAccountName.length() ==0){
                newBankClientHandler.sendOutput("Account name must not be blank, please try again.");
                continue;
            }
            //if account name already exists, error message and prompts new input,
            //else exit while loop
            if (!customer.checkExistingAccount(newAccountName)) {
                valid = true;
            }
            else{
                newBankClientHandler.sendOutput("Account name is taken, please try again.");
            }
        }
        //creates new account, defaults to balance of 0.0
        customer.addAccount(new Account(newAccountName, 0.0));
        return "SUCCESS";
    }

    private boolean checkQuitInput(String s){
        return s.equalsIgnoreCase("Q");
    }

}
