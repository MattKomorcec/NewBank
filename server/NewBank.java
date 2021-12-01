package newbank.server;

import java.util.HashMap;
import java.util.Map.Entry;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;

	//Initialising AccountManagement Object
	private AccountManagement accountManagement = new AccountManagement();

	// Initialising Transaction Object
	private Transaction transaction = new Transaction();

	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	private void addTestData() {
		Customer bhagy = new Customer("Bhagy", "1234");
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put(bhagy.getUsername(), bhagy);

		Customer christina = new Customer("Christina", "1234");
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put(christina.getUsername(), christina);

		Customer john = new Customer("Johh","1234");
		john.addAccount(new Account("Checking", 250.0));
		customers.put(john.getUsername(), john);
	}

	public static NewBank getBank() {
		return bank;
	}

	public synchronized Customer checkLogInDetails(String username, String password, NewBankClientHandler newBankClientHandler) {
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
	public synchronized String processRequest(Customer customer, String request, NewBankClientHandler newBankClientHandler) {
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

	public Customer getCustomer(String s){
		return customers.get(s);
	}
}
