package newbank.server;

import java.util.HashMap;
import java.util.Map.Entry;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;

	//initialised AccManagement Object
	private AccManagement accManagement= new AccManagement();
	//initialised Transaction Object
	private Transaction transaction = new Transaction();

	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}
	
	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
		
		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}
	
	public static NewBank getBank() {
		return bank;
	}
	
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request, NewBankClientHandler newBankClientHandler) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
				case "1" : return accManagement.showMyAccounts(customer);
				case "2" : return accManagement.newAccount(customer, newBankClientHandler);
				case "3" : return transaction.moveFunds(customer, newBankClientHandler);
				case "4" : return transaction.payFunds(customer, newBankClientHandler);
			default : return "FAIL";
			}
		}
		return "FAIL";
	}

	//added getter method for customers HashMap
	public HashMap<String,Customer> getCustomers(){
		return customers;
	}

	//added getter method for
	public String getID(Customer c) {
		for (Entry<String, Customer> entry : customers.entrySet()) {
			if (entry.getValue().equals(c)) {
				return entry.getKey();
			}
		}
		return "";
	}
}
