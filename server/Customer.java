package newbank.server;

import java.util.ArrayList;

public class Customer {

	private final int id;
	private final String dob;
    private final String username;
    private String password;
	private String secretAnswer;
	private boolean accountLocked;
	private String fullName;
    private ArrayList<Account> accounts;
    Database database = new Database();

	public Customer(int id, String dob, String username, String password, String secretAnswer,
					boolean accountLocked, String fullName) {

		accounts = new ArrayList<>();
		this.id = id;
		this.dob = dob;
		this.username = username;
		this.password = password;
		this.secretAnswer = secretAnswer;
		this.accountLocked = accountLocked;
		this.fullName = fullName;

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

	public void setAccounts(ArrayList<Account> accountArrayList){
		accounts = accountArrayList;
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

	public int getID() {
		return id;
	}

    public String getUsername() {
        return username;
    }

	public String getPassword() {
		return password;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean bool){
		accountLocked = bool;
		int value = (bool) ? 1 : 0;
		try {
			database.setLockAccount(username, value);
		}catch (Exception e){
			System.out.println("EXCEPTION!! Database.java: " + e.getMessage());
		}
	}


}
