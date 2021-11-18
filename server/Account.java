package newbank.server;

public class Account {

	private String accountName;
	private double balance;

	public Account(String accountName, double balance) {
		this.accountName = accountName;
		this.balance = balance;
	}

	public String toString() {
		return (accountName + ": " + balance);
	}

	//add getter for accountName
	public String getAccountName() {
		return accountName;
	}

	//add getter for opening balance
	public double getBalance() {
		return balance;
	}

	//add setter for opening balance
	public void setBalance(double d) {
		balance = d;
	}

}

