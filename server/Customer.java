package newbank.server;

import java.util.ArrayList;

public class Customer {

	private boolean accountLocked;

	private ArrayList<Account> accounts;
	String username;
	String password;

	public Customer(String username, String password) {
		accounts = new ArrayList<>();
		this.username = username;
		this.password = password;
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

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setAccountLocked(boolean value){
		accountLocked = value;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

}
