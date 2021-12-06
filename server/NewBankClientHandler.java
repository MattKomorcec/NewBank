package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	private ArrayList<String> failedLogInUsers = new ArrayList<>();


	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	public void run() {
		out.println("+------------------------------------+");
		out.println("|Welcome to NewBank                  |");
		out.println("+------------------------------------+");
		try {
			while (true) {
				printInitialMenu();
				switch (in.readLine()) {
					case "1":

						//If 3 failed login attempts during session, does not allow login
						if (failedLogInUsers.size()>4) {
							out.println("There have been too many unsuccessful login attempts, " +
									"please disconnect and try again later.");
							break;
						}

						while (failedLogInUsers.size()<5) {
							// Ask for username
							out.println("Enter Username:");
							String userName = in.readLine();

							if (isAccountLocked(userName)){
								out.println("This account is locked.");
								break;
							}

							// Ask for password
							out.println("Enter Password:");
							String password = in.readLine();
							out.println("\nChecking Details...");

							// Authenticate user and get customer ID token from bank for use in subsequent requests.
							Customer customer = bank.checkLogInDetails(userName, password, this);

							// If the user is authenticated then get requests from the user and process them.
							if (customer != null) {
								out.println("\nLoading...\n");
								out.println("Log In Successful.\n");
								out.println("Welcome to your NewBank account " + customer.getUsername()+".");
								printCustomerMenu();
								while (true) {
									String request = in.readLine();
									out.println("Request from " + customer.getUsername());
									// Added pass newBankClientHandler Object into processRequest, so it can call methods
									String response = bank.processRequest(customer, request, this);
									out.println(response);
								}
							} else {
								out.println("Loading...\n");
								out.println("Log In Failed.\n");

								//adds the string of username to failed login list.
								failedLogInUsers.add(userName);
								Map logInFrequencyMap = userLogInFrequency();
								lockUsers(logInFrequencyMap, 3);
							}
						}

						break;
					case "2":
						break;
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			}
			catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	// Adding getInput method, keeps input handling in NewBankClientHandler class.
	public String getInput() {

		try {
			String input = in.readLine();
			return input;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Adding sendOutput method, keeps input handling in NewBankClientHandler class.
	public void sendOutput(String s) {
		try {
			out.println(s);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	// Printing Initial Menu.
	private void printInitialMenu () {
		out.println("+------------------------------------+");
		out.println("|NewBank - Initial Menu              |");
		out.println("+------------------------------------+");
		out.println("|(1) - Login as existing customer    |");
		out.println("|(2) - Register for a new account    |");
		out.println("+------------------------------------+");
	}

	// Printing Customer Menu.
	public String printCustomerMenu () {
		out.println("\n+---------------------------------------------------+");
		out.println("|NewBank - Customer Menu                            |");
		out.println("+---------------------------------------------------+");
		out.println("|(1) - Show existing accounts and current balance   |");
		out.println("|(2) - Create a new account with zero opening funds |");
		out.println("|(3) - Move funds between your existing accounts    |");
		out.println("|(4) - Pay an existing NewBank customer             |");
		out.println("|(5) - Remove an existing account                   |");
		out.println("+---------------------------------------------------+");
		return "\n";
	}

	//counts the frequency of usernames for which logins have been attempted and checks against maximum attempts set
	private Map<String, Integer> userLogInFrequency(){
		Map<String, Integer> countMap = new HashMap<>();

		//adds each unique string to HashMap and counts the frequency
		for(String s : failedLogInUsers){
			Integer i = countMap.get(s);
			countMap.put(s, (i == null) ? 1 : i + 1);
		}
		return countMap;
	}

	private void lockUsers(Map<String, Integer> countMap, int maxAttempts){

		//iterates through the map of login frequencies
		//if user login number>maxAttempts and the account is not already locked, it locks it and prints message
		for (Map.Entry<String, Integer> set : countMap.entrySet()){

			Integer userAttempts = set.getValue();
			String customer = set.getKey();
			HashMap customers = NewBank.getBank().getCustomers();

			if (userAttempts >= maxAttempts){
				if (customers.containsKey(customer) &&
						!isAccountLocked(customer)) {
					NewBank.getBank().getCustomer(customer).setAccountLocked(true);
					out.println("The account: " + customer + " has been locked.\n");
				}
			}
		}
	}

	/*
    checks if account is locked, note will only be useful once database is running and
    the database stores accountLocked variable. Currently resetting connection resets
    the customer HashMap
	*/
	private boolean isAccountLocked(String userName){
		return NewBank.getBank().getCustomer(userName).isAccountLocked();
	}

}
