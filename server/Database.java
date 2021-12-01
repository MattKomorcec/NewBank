package newbank.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static final String connectionString = "jdbc:sqlite:users.db";
    private Connection conn = null;

    /**
     * Returns all customers from the database.
     *
     * @return All customers
     * @throws SQLException Thrown exception
     */
    public List<Customer> getAllCustomers() throws SQLException {
        try {
            // Opens a connection to the database
            conn = DriverManager.getConnection(connectionString);

            // SQL query that gets all entries from the users table
            String query = "SELECT * FROM users";
            PreparedStatement statement = conn.prepareStatement(query);

            // Executes the query, and retrieves the results
            ResultSet results = statement.executeQuery();

            ArrayList<Customer> customers = new ArrayList<>();

            while (results.next()) {
                // Gets all the values from the results of running the query
                int id = results.getInt("id");
                String name = results.getString("name");
                int accountNumber = results.getInt("account_number");

                // Creates an object of a customer class, using the retrieved values
                Customer customer = new Customer(name, String.valueOf(accountNumber));

                customers.add(customer);
                System.out.println(id + ". " + name + ", " + accountNumber);
            }

            return customers;
        } catch (Exception e) {
            System.out.println("EXCEPTION!! Database.java: " + e.getMessage());
            return null;
        } finally {
            // Closes the database connection if it's not closed already
            if (conn != null) {
                conn.close();
            }
        }
    }
}
