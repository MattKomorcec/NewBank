package newbank.server;

public class Account {
    private String accountNumber;
    private final AccountType accountType;
    private double balance;
    private String sortCode;

    // Constructor method for creating an object from data in the database.
    public Account(String accountNumber, AccountType accountType, double balance, String sortCode) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.sortCode = sortCode;
    }

    public String toString() {
        return ("AN: "+accountNumber + "  -  " +"SC: "+ sortCode +"  -  " + "AT: " + accountType + "  -  " + "B :£" + balance);
    }

    public String getAccountNumber() {
        return accountNumber;
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

    public String getSortCode() {
        return sortCode;
    }

    public enum AccountType {
        MAIN,
        SAVINGS,
        INVESTMENTS
    }
}
