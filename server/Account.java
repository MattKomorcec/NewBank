package newbank.server;

import java.util.Arrays;
import java.util.List;

public class Account {

	private String accountName;
	enum AccountType {MAIN, SAVINGS, INVESTMENTS};
	private final AccountType accountType;
	private double balance;

	public Account(AccountType accountType, double balance) {
		this.accountType = accountType;
		this.balance = balance;
	}

	public String toString() {
		return (accountType + " : £"+balance);
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double d) {
		balance = d;
	}
}

