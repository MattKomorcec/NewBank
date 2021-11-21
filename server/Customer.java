package newbank.server;

import java.util.ArrayList;

public class Customer {

	private ArrayList<newbank.server.Account> accounts;
	String username;
	String password;

	public Customer(String username, String password) {
		accounts = new ArrayList<>();
		this.username = username;
		this.password = password;
	}

	public String accountsToString() {
		String s = "";
		for (newbank.server.Account a : accounts) {
			s += a.toString();
			//add new line if more than one account
			if (accounts.size() > 1) {
				s += "\n";
			}
		}
		return s;
	}

	public void addAccount(newbank.server.Account account) {
		accounts.add(account);
	}

	public ArrayList<newbank.server.Account> getAccounts() {
		return accounts;
	}

	public boolean checkExistingAccount(String accountName) {
		for (newbank.server.Account a : accounts) {
			if (a.getAccountName().equals(accountName)) {
				return true;
			}
		}
		return false;
	}

	public newbank.server.Account getExistingAccount(String accountName) {
		for (newbank.server.Account a : accounts) {
			if (a.getAccountName().equals(accountName)) {
				return a;
			}
		}
		return null;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
