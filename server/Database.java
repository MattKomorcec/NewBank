package newbank.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static final String DB_CONNECTION_STRING = "jdbc:sqlite:database.db";
    private final boolean debug = false;
    private Connection conn = null;
    ArrayList<Customer> customers = new ArrayList<>();

    /**
     * Returns all customers from the database.
     *
     * @return All customers
     * @throws SQLException Thrown exception
     */
    public List<Customer> getAllCustomers() throws SQLException {
        try {

            openConnection();

            // SQL query that gets all entries from the users table
            String query = "SELECT * FROM users";
            PreparedStatement statement = conn.prepareStatement(query);

            // Executes the query, and retrieves the results
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                // Gets all the values from the results of running the query
                int id = results.getInt("id");
                String dob = results.getString("dob");
                String username = results.getString("username");
                String password = results.getString("password");
                String secretAnswer = results.getString("secret_answer");
                boolean accountLocked = results.getBoolean("account_locked");
                String fullName = results.getString("full_name");

                // Creates an object of a customer class, using the retrieved values
                Customer customer = new Customer(id, dob, username, password, secretAnswer, accountLocked, fullName);

                customers.add(customer);

                // Adds an ArrayList of the customer's accounts
                customer.setAccounts(getCustomerAccounts(id));

                if (debug) {
                    System.out.println(id + ". " + username);
                }
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION!! Database.java: " + e.getMessage());
            return null;

        } finally {
            closeConnection();
        }

        return customers;
    }

    // Returns an ArrayList of Account objects for a given user ID
    public ArrayList<Account> getCustomerAccounts(int userNumber) throws SQLException {

        ArrayList<Account> accounts = new ArrayList<>();
        try {

            // SQL query that gets all entries from the users table
            String query = "SELECT * FROM accounts WHERE user_id = ? ";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, userNumber);

            // Executes the query, and retrieves the results
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                // Gets all the values from the results of running the query
                String accountNumber = results.getString("account_number");
                String accountType = results.getString("account_type");
                int balance = results.getInt("balance");
                String sortCode = results.getString("sortcode");
                int userID = results.getInt("user_id");

                Account account = new Account(accountNumber, accountType, balance, sortCode, userID);
                accounts.add(account);

                if (debug) {
                    System.out.println(userID + ". " + accountNumber);
                }
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION!! Database.java: " + e.getMessage());
            return null;
        }
        return accounts;
    }

    /**
     * Creates a new user and inserts it into the database.
     *
     * @param dob            Date of birth
     * @param username       Desired username
     * @param password       Desired password
     * @param secretQuestion User's secret question
     * @param fullName       Full name
     * @return User's id or null if there was a problem
     * @throws SQLException If an exception occurs
     */
    public Integer createUser(String dob, String username, String password, String secretQuestion, String fullName) throws SQLException {
        try {
            // 0 - false, 1 - true
            int ACCOUNT_LOCKED_INITIAL = 0;

            openConnection();

            String addNewUser = "INSERT INTO users(dob, username, password, secret_answer, account_locked, full_name) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement updateUserdb = conn.prepareStatement(addNewUser);

            updateUserdb.setString(1, dob);
            updateUserdb.setString(2, username);
            updateUserdb.setString(3, password);
            updateUserdb.setString(4, secretQuestion);
            updateUserdb.setInt(5, ACCOUNT_LOCKED_INITIAL);
            updateUserdb.setString(6, fullName);

            updateUserdb.executeUpdate();

            conn.commit();

            String userIdQuery = "SELECT last_insert_rowid();";
            PreparedStatement statement = conn.prepareStatement(userIdQuery);
            Integer userId = null;
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                userId = result.getInt(1);
            }

            return userId;

        } catch (Exception e) {
            System.out.println("EXCEPTION!! Database.java: " + e.getMessage());
            conn.rollback();
            return null;

        } finally {
            closeConnection();
        }
    }

    /**
     * Creates a new account and associates it with a user.
     *
     * @param accountNumber Account number
     * @param accountType   Account type
     * @param sortCode      Sort code
     * @param balance       Balance
     * @param userId        Id of the user the account belongs to
     * @return Returns the success of the operation
     * @throws SQLException If an exception occurs
     */
    public boolean createAccount(String accountNumber, Account.AccountType accountType, String sortCode, int balance, int userId) throws SQLException {
        try {

            openConnection();

            String query = "INSERT INTO accounts(account_number, account_type, balance, sortcode, user_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, accountNumber);
            statement.setString(2, accountType.toString());
            statement.setInt(3, balance);
            statement.setString(4, sortCode);
            statement.setInt(5, userId);

            statement.executeUpdate();

            conn.commit();

            return true;
        } catch (Exception e) {
            System.out.println("EXCEPTION!! Database.java: " + e.getMessage());
            conn.rollback();
            return false;

        } finally {
            closeConnection();
        }
    }

    //sets the account lock status given the unique username and either 0 (unlocked) or 1 (locked)
    public void setLockAccount(String username, int value) throws SQLException {
        try {

            openConnection();

            String update = "UPDATE users SET account_locked = ? WHERE username = ? ";
            PreparedStatement statement = conn.prepareStatement(update);

            statement.setInt(1, value);
            statement.setString(2, username);

            statement.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            System.out.println("EXCEPTION!! Database.java: " + e.getMessage());
            conn.rollback();

        } finally {
            closeConnection();
        }
    }

    public void updateBalance(int id, String accountType, int balance) throws SQLException {
        try{
            openConnection();

            String update = "UPDATE accounts SET balance = ? WHERE user_id = ? AND account_type = ? ";
            PreparedStatement statement = conn.prepareStatement(update);

            statement.setInt(1, balance);
            statement.setInt(2, id);
            statement.setString(3, accountType);

            statement.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            System.out.println("EXCEPTION!! Database.java: " + e.getMessage());
            conn.rollback();

        } finally {
            closeConnection();
        }
    }

    public boolean checkColumnContainsValue(int id, String columnName, String valueToFind) throws SQLException {

        boolean exist = false;
        try{
            openConnection();

            String query = "SELECT EXISTS(SELECT * FROM accounts WHERE (accounts.id = ? AND ? = ?))";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setInt(1, id);
            statement.setString(2, columnName);
            statement.setString(3, valueToFind);

            ResultSet result = statement.executeQuery();
            exist = result.getBoolean(1);

        }catch (Exception e){
            System.out.println("EXCEPTION!! Database.java: " + e.getMessage());
            conn.rollback();

        }finally{
            closeConnection();
        }
        return exist;
    }

    private void openConnection() throws SQLException {
        // Opens a connection to the database
        conn = DriverManager.getConnection(DB_CONNECTION_STRING);
        conn.setAutoCommit(false);
    }

    private void closeConnection() throws SQLException{
        // Closes the database connection if it's not closed already
        try {
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("EXCEPTION!! Database.java: " + e.getMessage());
        }
    }
}
