package newbank.server;

import java.util.List;

public class NewBank {

    private static final NewBank bank = new NewBank();
    private Database db = new Database();
    private List<Customer> customers;

    //Initialising AccountManagement Object
    private AccountManagement accountManagement = new AccountManagement();

    // Initialising Transaction Object
    private Transaction transaction = new Transaction();

    private NewBank() {
        try {
            customers = db.getAllCustomers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NewBank getBank() {
        return bank;
    }

    public synchronized Customer checkLogInDetails(String username, String password, NewBankClientHandler newBankClientHandler) {

        for (Customer c : customers) {
            if (c.getUsername().equals(username)) {

                if (c.getPassword().equals(password)) {
                    newBankClientHandler.sendOutput("-Username and Password correct");
                    return c;
                } else {
                    newBankClientHandler.sendOutput("-Wrong password\n");
                    return null;
                }
            }
        }
        newBankClientHandler.sendOutput("-Wrong username\n");
        return null;
    }

    // Commands from the NewBank customer are processed in this method.
    public synchronized String processRequest(Customer customer, String request, NewBankClientHandler newBankClientHandler) {
        if (customers.contains(customer)) {
            switch (request) {
                case "1":
                    return accountManagement.showMyAccounts(customer, newBankClientHandler);
                case "2":
                    return accountManagement.newAccount(customer, newBankClientHandler);
                case "3":
                    return transaction.moveFunds(customer, newBankClientHandler);
                case "4":
                    return transaction.payFunds(customer, newBankClientHandler);
                case "5":
                    return accountManagement.removeAccount(customer, newBankClientHandler);
                default:
                    return "FAIL";
            }
        }
        return "FAIL";
    }

    public Customer getCustomer(String username) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username)) {
                return c;
            }
        }
        return null;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
/*
	public String getID(Customer c) {
		for (Entry<String, Customer> entry : customers.entrySet()) {
			if (entry.getValue().equals(c)) {
				return entry.getKey();
			}
		}
		return "";
	}

 */

}