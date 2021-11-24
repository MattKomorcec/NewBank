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

	public String getAccountName() {
		return accountName;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double d) {
		balance = d;
	}

}

