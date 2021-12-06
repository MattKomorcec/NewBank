package newbank.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class UserRegistration {

    private final Scanner sc = new Scanner(System.in);
    private final int ACCOUNT_LOCKED_INITIAL = 0; // 0 - false, 1 - true

    public UserRegistration newUser() throws SQLException {
        String UserFullname = getNameFromUser();
        String UserDob = getDateFromUser();
        String username = getUsername();
        String UserAccountNumber = getNewUserAccountNumber();
        String UserSortcode = getNewUserSortCode();
        String UserPassword = getNewUserInputNewPassword();
        int UserBalance = getNewUserBalance();
        String UserSecretQuestion = getUserSecretQuestion();

        Connection conn = null;
        try {
            String url = "jdbc:sqlite:database.db";
            conn = DriverManager.getConnection(url);
            String addNewUser = "INSERT INTO users(dob, username, password, account_number, sortcode, balance, secret_answer, account_locked, full_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // New values passed to db.
            PreparedStatement updateUserdb = conn.prepareStatement(addNewUser);

            updateUserdb.setString(1, UserDob);
            updateUserdb.setString(2, username);
            updateUserdb.setString(3, UserPassword);
            updateUserdb.setString(4, UserAccountNumber);
            updateUserdb.setString(5, UserSortcode);
            updateUserdb.setInt(6, UserBalance);
            updateUserdb.setString(7, UserSecretQuestion);
            updateUserdb.setInt(8, ACCOUNT_LOCKED_INITIAL);
            updateUserdb.setString(9, UserFullname);
            updateUserdb.executeUpdate();

            System.out.println("User account successfully created, you may now login.");

            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return null;
    }

    private String getNameFromUser() {
        String newName;
        String newFirstName;
        String newLastName;

        System.out.println("\n");
        System.out.print("                                               Welcome to NewBank Registration");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n Please ensure your terminal is fully open to ensure full user experience");
        System.out.print("\nPlease enter your first name: ");
        newFirstName = sc.nextLine().toUpperCase();

        System.out.print("\nPlease enter your last name: ");
        newLastName = sc.nextLine().toUpperCase();
        newName = newFirstName + " " + newLastName;
        System.out.print("\nThe name associated with this account will be: " + newName);
        String response;
        do {
            System.out.print("\nIs this correct (Y/N)?: ");
            response = sc.nextLine().toUpperCase();
            if (response.equals("N")) {
                System.out.print("Ok, you would like to change the name for the account");
                System.out.print("\nWhat will the new first name be: ");
                newFirstName = sc.nextLine().toUpperCase();
                System.out.println("\nWhat will the new last name be: ");
                newLastName = sc.nextLine().toUpperCase();
                newName = newFirstName + " " + newLastName;
                System.out.print("\nThe name associated with this account will be: " + newName);
                System.out.print("\nIs this correct (Y/N)?: ");
                response = sc.nextLine().toUpperCase();
            }
        } while (!response.equals("Y"));
        return newName;
    }

    private String getDateFromUser() {
        boolean isItDate;
        String enteredDate;
        String userDateResponse;
        do {
            System.out.print("For your date of birth please enter in the format of (DD/MM/YYYY) including /");
            System.out.println("\nDate of Birth: ");
            enteredDate = sc.nextLine();
            System.out.println("\nThe date of birth is set to: " + enteredDate);
            isItDate = isDateValid(enteredDate);
        } while (!isItDate);
        do {
            System.out.println("Is the date of birth correct (Y/N)?: ");
            userDateResponse = sc.nextLine().toUpperCase();
            if (userDateResponse.equals("N")) {
                System.out.print("\nPlease create a new Date of birth: ");
                enteredDate = sc.nextLine();
                System.out.println("\nThe date of birth has been updated to:");
                System.out.println("\nConfirm new date of birth(Y/N): " + enteredDate);
                userDateResponse = sc.nextLine().toUpperCase();
            }
        } while (!userDateResponse.equals("Y"));
        return enteredDate;
    }

    private boolean isDateValid(String enteredDate) {
        enteredDate = enteredDate + " 00:00:00";
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Date date;
        try {
            date = df.parse(enteredDate);
        } catch (ParseException e) {
            System.out.println("Sorry invalid format date input please try again");
            return false;
        }
        return isUserEighteen(date);
    }

    private boolean isUserEighteen(Date dateToCheck) {
        Calendar doB = Calendar.getInstance();
        doB.setTime(dateToCheck);
        doB.add(Calendar.YEAR, 18);
        Date newDate = doB.getTime();
        Date dateToday = new Date();
        if (newDate.compareTo(dateToday) < 0) {
            return true;
        }
        System.out.println("\nThe data entered means you are less than 18.");
        return false;
    }

    private String getUsername() {
        String userAnswer;
        String newUsername;
        do {
            System.out.print("\nPlease create a new username: ");
            newUsername = sc.nextLine();
            System.out.println("\nIs the username correct(Y/N): " + newUsername);
            userAnswer = sc.nextLine().toUpperCase();
        } while (userAnswer.equals("N"));
        System.out.println("\n The username is set to: " + newUsername);
        return newUsername;
    }

    private String getNewUserAccountNumber() {
        int min = 11121211;
        int max = 99879199;
        int accountNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        System.out.println("\n");
        System.out.println("\n The new account the account number and sortcode will be:");
        System.out.println("                 Account Number: " + accountNumber);
        return String.valueOf(accountNumber);
    }

    private String getNewUserSortCode() {
        int min = 111212;
        int max = 998791;
        int accountSortCode = (int) Math.floor(Math.random() * (max - min + 1) + min);
        System.out.println("                 Sortcode: " + accountSortCode);
        return String.valueOf(accountSortCode);
    }

    private String getNewUserInputNewPassword() {
        //Boolean password conditions = to check that:
        // Contains more than 8 chars.
        // Contains at least one numerical value.
        // Contains at least one capital letter.
        // Contains at least one lowercase letter.
        String newPassword;
        boolean passwordChecks;
        String userAnswerPassword;
        do {
            passwordChecks = true;
            System.out.println("\n");
            System.out.println("\nYou will now need to create a password for this account");
            System.out.print("\nThe password must be (At least 8 letters long, contain a capital letter, a lowercase letter and numerical value)");
            System.out.println("\nPlease create your new password: ");
            newPassword = sc.nextLine();
            if (!passwordLength(newPassword)) {
                passwordChecks = false;
            }
            if (!doesPasswordFulfillChecks(newPassword)) {
                passwordChecks = false;
            }
        } while (!passwordChecks);

        do {
            System.out.println("\n Your new password is: " + newPassword);
            System.out.println("\nConfirm password (Y/N): ");
            userAnswerPassword = sc.nextLine().toUpperCase();
            if (userAnswerPassword.equals("N")) {
                System.out.println("\nThe password must be (At least 8 letters long, contain a capital letter, a lowercase letter and numerical value)");
                System.out.println("\nPlease create your new password: ");
                newPassword = sc.nextLine();
                System.out.println("Ok the password has now been updated: " + newPassword);
                System.out.println("\nConfirm new password(Y/N): ");
                userAnswerPassword = sc.nextLine().toUpperCase();
            }
        } while (userAnswerPassword.equals("N"));

        System.out.println("\nYour new password is: " + newPassword);
        return newPassword;
    }

    private boolean passwordLength(String newPassword) {
        if (newPassword.length() > 7) {
            return doesPasswordFulfillChecks(newPassword);
        } else {
            System.out.println("\nSorry the password " + newPassword + " does not meet the requirements");
            return false;
        }
    }

    private boolean doesPasswordFulfillChecks(String newPassword) {
        boolean numericalCheck = false;
        boolean capitalCheck = false;
        boolean lowerCaseCheck = false;
        char c;
        // For the password to be accepted all conditions must be true otherwise it will not be.
        for (int i = 0; i < newPassword.length(); i++) {
            c = newPassword.charAt(i);
            if (Character.isDigit(c)) {
                numericalCheck = true;
            }
            if (Character.isLowerCase(c)) {
                lowerCaseCheck = true;
            } else if (Character.isUpperCase(c)) {
                capitalCheck = true;
            }
            if (numericalCheck && lowerCaseCheck && capitalCheck) {
                return true;
            }
        }
        return false;
    }

    private int getNewUserBalance() {
        String userOpenBalance;
        String response;
        int newBalance = 0;
        boolean inputCorrect = false; // Assumption that user input may be false.
        do {
            System.out.println("\nPlease enter your opening balance (£xxx): ");
            userOpenBalance = sc.nextLine();
            try {
                newBalance = Integer.parseInt(userOpenBalance);
                if (newBalance > 0) {           //Checks to see if user has entered an amount greater than 0.
                    System.out.println("New Account Balance: £" + newBalance);
                    inputCorrect = true;    // If greater than 0, user will continue else user will be prompted to enter a valid amount.
                }
            } catch (Exception e) {
                System.out.println("\nThis is not a correct value input");
                inputCorrect = false;
            }
        }
            while (!inputCorrect) ;
            do {
                System.out.print("\nIs this correct (Y/N)?: ");
                response = sc.nextLine().toUpperCase();
                if (response.equals("N")) {
                    System.out.print("Ok,what would you like the update opening balance to be?: ");
                    userOpenBalance = sc.nextLine();
                    try {
                        newBalance = Integer.parseInt(userOpenBalance);
                        System.out.println("New Account Balance: £" + newBalance);
                    } catch (Exception e) {
                        System.out.println("\nThis is not a correct value input");
                    }
                    System.out.print("\nIs this correct (Y/N)?: £" + userOpenBalance);
                    response = sc.nextLine().toUpperCase();
                }
            } while (!response.equals("Y"));

            return newBalance;
        }



        private String getUserSecretQuestion () {
            String secretQuestionAnswer;
            String response;
            System.out.println("\n");
            System.out.println("\nPlease now answer the following secret question");
            System.out.println("\nWhat was your first pets name?: ");
            secretQuestionAnswer = sc.nextLine();
            System.out.println("The secret answer is set to: " + secretQuestionAnswer);
            do {
                System.out.print("\nIs this correct (Y/N)?: ");
                response = sc.nextLine().toUpperCase();
                if (response.equals("N")) {
                    System.out.print("Ok, you would like to change the secret answer to?: ");
                    secretQuestionAnswer = sc.nextLine();
                    System.out.print("\nThe secret answer associated with this account will be:" + " " + secretQuestionAnswer);
                    System.out.print("\nIs this correct (Y/N)?: ");
                    response = sc.nextLine().toUpperCase();
                }
            } while (!response.equals("Y"));
            return secretQuestionAnswer;
        }

        private boolean getUserCheckHuman () {
            //No condition or iterations as checks once to ensure the user is human.
            //Generates a random number between 1-10 for each multiplication value.
            int answerToQuestion;
            int userAnswer;
            int min = 1;
            int max = 10;
            int number1 = (int) Math.floor(Math.random() * (max - min + 1) + min);
            int number2 = (int) Math.floor(Math.random() * (max - min + 1) + min);
            answerToQuestion = number1 * number2;

            System.out.println("\nPlease now complete the following question");
            System.out.println("What is " + number1 + " x " + number2 + " = ");
            userAnswer = sc.nextInt();

            if (userAnswer == answerToQuestion) {
                System.out.println("\nNew User passed security check");
                System.out.println("\nUser now registered");
                System.out.println("\nYou can now log in to your NewBank account");
                return true;
            }
            System.out.println("\nYou have failed the security check, and cannot register an account with us today");
            return false;
        }
    }
