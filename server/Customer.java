package newbank.server;

import java.util.ArrayList;

public class Customer extends User {

	private boolean accountLocked;
	private ArrayList<Account> accounts;

	public Customer(int id, String dob, String username, String password,
					String secretAnswer, int AccountLocked, String fullname) {

		super(id, dob, username, password, secretAnswer, AccountLocked, fullname);

		accounts = new ArrayList<>();
		retrieveAccounts();
	}

	public void retrieveAccounts() {
		Database database = new Database();
		this.accounts = database.getAccounts(getUserId());
	}

	public Customer retrieveCustomers() {
		Database database = new Database();
		return database.getCustomer(getUserId());
	}

	public String accountsToString() {
		String s = "";
		for (Account a : accounts) {
			s += a.toString();
			if (accounts.size() > 1) {
				s += "\n";
			}
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public void removeAccount (Account account) {
		accounts.remove(account);
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public boolean checkExistingAccount(String accountType) {
		for (Account a : accounts) {
			if (a.getAccountType().toString().equalsIgnoreCase(accountType)) {
				return true;
			}
		}
		return false;
	}

	public Account getExistingAccount(String accountType) {
		for (Account a : accounts) {
			if (a.getAccountType().toString().equalsIgnoreCase(accountType)) {
				return a;
			}
		}
		return null;
	}

	public void setAccountLocked(boolean value){
		accountLocked = value;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

}