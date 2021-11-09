package newbank.server;

import java.util.ArrayList;

public class Customer {
	
	private ArrayList<Account> accounts;
	
	public Customer() {
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
			//add new line if more than one account
			if (accounts.size()>1){
			s += "\n";
			}
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}

	//add getAccounts method
	public ArrayList<Account> getAccounts(){
		return accounts;
	}

	//checkExistingAccount
	public boolean checkExistingAccount(String accountName) {
		for (Account a : accounts) {
			if (a.getAccountName().equals(accountName)) {
				return true;
			}
		}
		return false;
	}

	//getExistingAccount
	public Account getExistingAccount(String accountName) {
		for (Account a : accounts) {
			if (a.getAccountName().equals(accountName)) {
				return a;
			}
		}
		return null;
	}

}
