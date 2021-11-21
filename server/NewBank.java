package newbank.server;

import java.util.HashMap;
import java.util.Map.Entry;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, newbank.server.Customer> customers;

	//Initialising AccountManagement Object
	private newbank.server.AccountManagement accountManagement = new newbank.server.AccountManagement();

	// Initialising Transaction Object
	private Transaction transaction = new Transaction();

	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	private void addTestData() {
		newbank.server.Customer bhagy = new newbank.server.Customer("Bhagy", "1234");
		bhagy.addAccount(new newbank.server.Account("Main", 1000.0));
		customers.put(bhagy.getUsername(), bhagy);

		newbank.server.Customer christina = new newbank.server.Customer("Christina", "1234");
		christina.addAccount(new newbank.server.Account("Savings", 1500.0));
		customers.put(christina.getUsername(), christina);

		newbank.server.Customer john = new newbank.server.Customer("Johh","1234");
		john.addAccount(new newbank.server.Account("Checking", 250.0));
		customers.put(john.getUsername(), john);
	}

	public static NewBank getBank() {
		return bank;
	}

	public synchronized newbank.server.Customer checkLogInDetails(String username, String password, NewBankClientHandler newBankClientHandler) {
		if (customers.containsKey(username) && customers.get(username).getPassword().equals(password)) {
			newBankClientHandler.sendOutput("-Username correct");
			newBankClientHandler.sendOutput("-Password correct");
			return customers.get(username);
		}
		else if (!customers.containsKey(username)){
			newBankClientHandler.sendOutput("-Wrong username\n");
			return null;
		}
		else if (!customers.get(username).getPassword().equals(password)) {
			newBankClientHandler.sendOutput("-Wrong password\n");
			return null;
		}
		else {
			return null;
		}
	}

	// Commands from the NewBank customer are processed in this method.
	public synchronized String processRequest(newbank.server.Customer customer, String request, NewBankClientHandler newBankClientHandler) {
		if (customers.containsKey(customer.getUsername())) {
			switch (request) {
				case "1":
					return accountManagement.showMyAccounts(customer);
				case "2":
					return accountManagement.newAccount(customer, newBankClientHandler);
				case "3":
					return transaction.moveFunds(customer, newBankClientHandler);
				case "4":
					return transaction.payFunds(customer, newBankClientHandler);
				default:
					return "FAIL";
			}
		}
		return "FAIL";
	}

	public HashMap<String, newbank.server.Customer> getCustomers() {
		return customers;
	}

	public String getID(newbank.server.Customer c) {
		for (Entry<String, newbank.server.Customer> entry : customers.entrySet()) {
			if (entry.getValue().equals(c)) {
				return entry.getKey();
			}
		}
		return "";
	}
}
