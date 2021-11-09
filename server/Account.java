package newbank.server;

public class Account {
	
	private String accountName;
	private double openingBalance;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}
	
	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	//add getter for accountName
	public String getAccountName(){
		return accountName;
	}

	//add getter for opening balance
	public double getOpeningBalance(){
		return openingBalance;
	}

	//add setter for opening balance
	public void setOpeningBalance(double d){
		openingBalance = d;
	}

}
