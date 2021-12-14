package newbank.server;

public class Account {

    private final String accountNumber;
    private final AccountType accountType;
    private int balance;
    private String sortCode;
    private int userID;

    public Account(String accountNumber, String accountType, int balance, String sortCode, int userID) {

        this.accountNumber = accountNumber;
        this.accountType = AccountType.valueOf(accountType);
        this.balance = balance;
        this.sortCode = sortCode;
        this.userID = userID;
    }

    public String toString() {
        return (accountType + " : £" + balance);
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int value) {
        balance = value;
    }

    public enum AccountType {
        MAIN,
        SAVINGS,
        INVESTMENTS
    }
}
