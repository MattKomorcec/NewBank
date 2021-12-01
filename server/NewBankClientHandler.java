package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	private int failedLogInCount = 0;
	private ArrayList<String> failedLogInUsers = new ArrayList<>();


	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	public void run() {
		out.println("+------------------------------------+");
		out.println("|Welcome to NewBank                  |");
		try {
			while (true) {
				showInitialMenu();
				switch (in.readLine()) {
					case "1":

						//If 3 failed login attempts during session, does not allow login
						if (failedLogInCount >2) {
							out.println("There have been too many unsuccessful login attempts, " +
									"please disconnect and try again later.");
							break;
						}

						while (failedLogInCount<3) {
							// Ask for username
							out.println("Enter Username:");
							String userName = in.readLine();
							// Ask for password
							out.println("Enter Password:");
							String password = in.readLine();
							out.println("\nChecking Details...");

							// Authenticate user and get customer ID token from bank for use in subsequent requests.
							Customer customer = bank.checkLogInDetails(userName, password, this);

							// If the user is authenticated then get requests from the user and process them.
							if (customer != null) {
								showCustomerMenu(customer);
								while (true) {
									String request = in.readLine();
									System.out.println("Request from " + customer.getUsername());
									// Added pass newBankClientHandler Object into processRequest, so it can call methods
									String response = bank.processRequest(customer, request, this);
									out.println(response);
								}
							} else {
								out.println("Loading...");
								out.println("Log In Failed.\n");
								//increases failed log in count by one and adds the string of username to failed login list.
								failedLogInCount += 1;
								failedLogInUsers.add(userName);
							}
						}

						//checks frequency of usernames entered
						String userToLock = userLogInFrequency(failedLogInUsers, 3);
						//if username entered x3 corresponds to actual username, locks it
						if (NewBank.getBank().getCustomers().containsKey(userToLock)) {
							NewBank.getBank().getCustomer(userToLock).setAccountLocked();
							out.println("The account: " + userToLock + " has been locked.");
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

	private void showInitialMenu(){
		out.println("+------------------------------------+");
		out.println("|NewBank - Initial Menu              |");
		out.println("+------------------------------------+");
		out.println("|(1) - Login as existing customer    |");
		out.println("|(2) - Register for a new account    |");
		out.println("+------------------------------------+");
	}

	private void showCustomerMenu(Customer customer){
		out.println("\nLoading...\n");
		out.println("Log In Successful.\n");
		out.println("Welcome to your NewBank account " + customer.getUsername()+".");
		out.println("\n+---------------------------------------------------+");
		out.println("|NewBank - Customer Menu                            |");
		out.println("+---------------------------------------------------+");
		out.println("|(1) - Show existing accounts and current balance   |");
		out.println("|(2) - Create a new account with zero opening funds |");
		out.println("|(3) - Move funds between your existing accounts    |");
		out.println("|(4) - Pay an existing NewBank customer             |");
		out.println("+---------------------------------------------------+");
	}

	//counts the frequency of usernames for which logins have been attempted and checks against maximum attempts set
	private String userLogInFrequency(ArrayList<String> failedLogInUsers, int maxAttempts){
		Map<String, Integer> countMap = new HashMap<String, Integer>();

		//adds each unique string to HashMap and counts
		for(String s : failedLogInUsers){
			Integer i = countMap.get(s);
			countMap.put(s, (i == null) ? 1 : i + 1);
		}

		//if number of times username has been attempted == maxAttempts then returns username
		for (Map.Entry<String, Integer> set : countMap.entrySet()){
			if (set.getValue() == maxAttempts){
				return set.getKey();
			}
		}
		return null;
	}

}
