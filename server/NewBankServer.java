package newbank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class NewBankServer extends Thread {

	private final ServerSocket server;

	public NewBankServer(int port) throws IOException {
		server = new ServerSocket(port);
	}

	public void run() {
		// starts up a new client handler thread to receive incoming connections and process requests
		System.out.println("New Bank Server listening on " + server.getLocalPort());
		try {
			while (true) {
				Socket s = server.accept();
				NewBankClientHandler clientHandler = new NewBankClientHandler(s);
				clientHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	public static void main(String[] args) throws IOException, SQLException {
    new NewBankServer(14005).start();
    
		// This code just demonstrates how to use the Database class -> can be removed at some point
		Database db = new Database();
		List<Customer> results = db.getAllCustomers();

		UserRegistration ur = new UserRegistration();
		//needed to call user reg class.
		ur = ur.newUser();
	}
}
