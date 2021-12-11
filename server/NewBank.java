package newbank.server;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class NewBank {

	private static final NewBank bank = new NewBank();
    private Database db = new Database();
	private List<Customer> customers;

	//Initialising AccountManagement Object
	private AccountManagement accountManagement = new AccountManagement();

	// Initialising Transaction Object
	private Transaction transaction = new Transaction();

	private NewBank() throws SQLException {
		customers = db.getAllCustomers();
		//addTestData();
	}

    public static NewBank getBank() {
        return bank;
    }

    /*
    private void addTestData() {
        Customer bhagy = new Customer("Bhagy", "1234");
        bhagy.addAccount(new Account(Account.AccountType.MAIN, 1000.0));
        customers.put(bhagy.getUsername(), bhagy);

		Customer christina = new Customer("Christina", "1234");
		christina.addAccount(new Account(Account.AccountType.SAVINGS, 1500.0));
		customers.put(christina.getUsername(), christina);

		Customer john = new Customer("Johh","1234");
		john.addAccount(new Account(Account.AccountType.INVESTMENTS, 250.0));
		customers.put(john.getUsername(), john);

        Customer matija = new Customer("matijak","Matijak1");
        matija.addAccount(new Account(Account.AccountType.MAIN, 1230.0));
        matija.addAccount(new Account(Account.AccountType.SAVINGS, 1000.0));
        customers.put(matija.getUsername(), matija);
	}
	*/

    public synchronized Customer checkLogInDetails(String username, String password, NewBankClientHandler newBankClientHandler) {
        if (customers.containsKey(username) && customers.get(username).getPassword().equals(password)) {
            newBankClientHandler.sendOutput("-Username correct");
            newBankClientHandler.sendOutput("-Password correct");
            return customers.get(username);
        } else if (!customers.containsKey(username)) {
            newBankClientHandler.sendOutput("-Wrong username\n");
            return null;
        } else if (!customers.get(username).getPassword().equals(password)) {
            newBankClientHandler.sendOutput("-Wrong password\n");
            return null;
        } else {
            return null;
        }
    }

    // Commands from the NewBank customer are processed in this method.
    public synchronized String processRequest(Customer customer, String request, NewBankClientHandler newBankClientHandler) {
        if (customers.containsKey(customer.getUsername())) {
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

    public HashMap<String, Customer> getCustomers() {
        return customers;
    }

	public String getID(Customer c) {
		for (Entry<String, Customer> entry : customers.entrySet()) {
			if (entry.getValue().equals(c)) {
				return entry.getKey();
			}
		}
		return "";
	}

	public Customer getCustomer(String username){
		return customers.get(username);
	}
}
