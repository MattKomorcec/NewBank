package newbank.server;

import com.sun.tools.javac.Main;

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

public class UserRegistration extends Main {


    public static UserRegistration newUser() throws SQLException {

        Scanner sc = new Scanner(System.in);
        // set all methods to return values - created conversions for sortcode
        // and account number to make it easier to push through to db.


        NewUserFullname nn = new NewUserFullname();
        String userFullname = nn.getNameFromUser();


        DateChecks dc = new DateChecks();
        String userDoB = String.valueOf(dc.getDateFromUser());


        NewAccountUsername ui = new NewAccountUsername();
        String userUsername = ui.getNewUserId();


        NewUserAccountNumber acm = new NewUserAccountNumber();
        String userAccountNumber = acm.getNewUserAccountNumber();


        NewUserSortcode nusort = new NewUserSortcode();
        String userSortcode = nusort.getNewUserSortCode();


        NewUserSecurity nus = new NewUserSecurity();
        String userNewPassword = nus.inputNewPassword();


        NewUserBalance nub = new NewUserBalance();
        Integer userNewBalance = Integer.valueOf(nub.balance());


        NewUserSecretQuestion nsq = new NewUserSecretQuestion();
        String userNewSecretAnswer = nsq.secretQuestion();


        CheckNotARobot cnr = new CheckNotARobot();
        boolean newUserNotARobot = cnr.checkHuman();

        // All already scanned by scanner then passed as values to 'String addNewUser'
        // The values are then passed to the db.


        Connection conn = null;

        try {
            String url = "jdbc:sqlite:identifier.sqlite";

            conn = DriverManager.getConnection(url);
            // Will allow for the database connection.

            String addNewUser = "INSERT INTO user_database(full_name, dob, username, account_number, sortcode, password, balance, secret_answer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            // New values passed to db.
            PreparedStatement updateUserdb = conn.prepareStatement(addNewUser);

            updateUserdb.setString(1, userFullname);
            updateUserdb.setString(2, userDoB);
            updateUserdb.setString(3, userUsername);
            updateUserdb.setString(4, userAccountNumber);
            updateUserdb.setString(5, userSortcode);
            updateUserdb.setString(6, userNewPassword);
            updateUserdb.setInt(7, userNewBalance);
            updateUserdb.setString(8, userNewSecretAnswer);


            updateUserdb.executeUpdate();
            conn.commit();
            // The db will now push the output.


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


    private static class NewUserFullname {

        Scanner sc = new Scanner(System.in);
        String newName;

        public String getNameFromUser() {

            System.out.println(" ");
            System.out.print("                                               Welcome to NewBank Registration");
            System.out.println(" ");
            System.out.println(" ");


            System.out.println("\n Please ensure your terminal is fully open to ensure full user experience");
            System.out.print("\nPlease enter your first name: ");
            String newFirstName = sc.nextLine().toUpperCase();

            System.out.print("\nPlease enter your last name: ");
            String newLastName = sc.nextLine().toUpperCase();
            newName = newFirstName + " " + newLastName;
            System.out.print("\nThe name associated with this account will be:" + " " + newName);
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
                    System.out.print("\nThe name associated with this account will be:" + " " + newName);
                    System.out.print("\nIs this correct (Y/N)?: ");
                    response = sc.nextLine().toUpperCase();
                }
            } while (!response.equals("Y"));
            return newName;
        }

    }


    private static class DateChecks {

        Scanner sc = new Scanner(System.in);
        boolean isItDate = false;
        String enteredDate;
        String userDateResponse;

        public String getDateFromUser() {
            do {
                System.out.print("For your date of birth please enter in the format of (DD/MM/YYYY) including /");
                System.out.println("\nDate of Birth: ");
                enteredDate = sc.nextLine();
                System.out.println("\nThe date of birth is set to:" + " " + enteredDate);
                isItDate = isDateValid(enteredDate);
            } while (!isItDate);

            do {
                System.out.println("Is the date of birth correct (Y/N)?: ");
                userDateResponse = sc.nextLine().toUpperCase();
                if (userDateResponse.equals("N")) {
                    System.out.print("\nPlease create a new Date of birth: ");
                    enteredDate = sc.nextLine();
                    System.out.println("\nThe date of birth has been updated to:");
                    System.out.println("\nConfirm new date of birth(Y/N): " + " " + enteredDate);
                    userDateResponse = sc.nextLine().toUpperCase();
                }
                } while (!userDateResponse.equals("Y"));
            return enteredDate;
            }
        }

        public static boolean isDateValid(String enteredDate) {

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

        public static boolean isUserEighteen(Date dateToCheck) {

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
    }

    class NewAccountUsername {
        Scanner sc = new Scanner(System.in);
        String userAnswer;
        String newUsername;

        public String getNewUserId() {
            do {
                System.out.print("\nPlease create a new username: ");
                newUsername = sc.nextLine();
                System.out.println("\nIs the username correct(Y/N): " + " " + newUsername);
                userAnswer = sc.nextLine().toUpperCase();
            } while (userAnswer.equals("N"));
            System.out.println("\n The username is set to:" + newUsername);
            return newUsername;
        }
    }

    class NewUserAccountNumber {
        public String getNewUserAccountNumber() {
            int min = 11121211;
            int max = 99879199;
            int accountNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
            System.out.println(" ");
            System.out.println("\n The new account the account number and sortcode will be:");
            System.out.println("                 Account Number:" + " " + accountNumber);
            return String.valueOf(accountNumber);
        }
    }

    class NewUserSortcode {
        public String getNewUserSortCode() {
            int min = 111212;
            int max = 998791;
            int accountSortCode = (int) Math.floor(Math.random() * (max - min + 1) + min);
            System.out.println("                 Sortcode:" + " " + accountSortCode);
            return String.valueOf(accountSortCode);
        }

    }

    class NewUserSecurity {

        Scanner sc = new Scanner(System.in);
        String newPassword;
        boolean passwordChecks;
        String userAnswerPassword;
        String userresponse;

        public String inputNewPassword() {
            do {
                passwordChecks = true;
                System.out.println(" ");
                System.out.println("\nYou will now need to create a password for this account");
                System.out.print("\nThe password must be (At least 8 letters long, contain a capital letter, a lowercase letter and numerical value)");
                System.out.println("\nPlease create your new password: ");
                newPassword = sc.nextLine();
                if (!passwordLength(newPassword)) {
                    passwordChecks = false;
                }
                if (!passwordChecks(newPassword)) {
                    passwordChecks = false;
                }
            } while (!passwordChecks);

            do {
                System.out.println("\n Your new password is:" + " " + newPassword);
                System.out.println("\nConfirm password (Y/N): ");
                userAnswerPassword = sc.nextLine().toUpperCase();
                if (userAnswerPassword.equals("N")) {
                    System.out.println("\nThe password must be (At least 8 letters long, contain a capital letter, a lowercase letter and numerical value)");
                    System.out.println("\nPlease create your new password: ");
                    newPassword = sc.nextLine();
                    System.out.println("Ok the password has now been updated:" + newPassword);
                    System.out.println("\nConfirm new password(Y/N): ");
                    userAnswerPassword = sc.nextLine().toUpperCase();
                }
            } while (!passwordChecks & userAnswerPassword.equals("N"));

            System.out.println("\n Your new password is:" + " " + newPassword);
            return newPassword;
        }


        public boolean passwordLength(String newPassword) {
            if (newPassword.length() > 7) {
                return passwordChecks(newPassword);
            } else {
                System.out.println(" ");
                System.out.println("\nSorry the password" + " " + newPassword + " " + "does not meet the requirements");
                return false;
            }
        }

        //Boolean password conditions = to check that:
        // Contains more than 8 chars.
        // Contains at least one numerical value.
        // Contains at least one capital letter.
        // Contains at least one lowercase letter.


        private boolean passwordChecks(String newPassword) {
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
    }

    class NewUserBalance {

        Scanner sc = new Scanner(System.in);
        String userOpenBalance;
        boolean isItAFloat;
        String response;
        float newBalance;

        public String balance() {
            do {
                isItAFloat = true;
                System.out.println("\nPlease enter your opening balance (xx.xx): ");
                userOpenBalance = sc.nextLine();
                try {
                    float newBalance = Float.parseFloat(userOpenBalance);
                    System.out.println("New Account Balance:" + "£" + newBalance);
                } catch (Exception e) {
                    isItAFloat = false;
                    System.out.println("\nThis is not a correct value input");
                }

            } while (!isItAFloat);

            do {
                System.out.print("\nIs this correct (Y/N)?: ");
                response = sc.nextLine().toUpperCase();
                if (response.equals("N")) {
                    System.out.print("Ok,what would you like the update opening balance to be?: ");
                    userOpenBalance = sc.nextLine();
                    try {
                        float newBalance = Float.parseFloat(userOpenBalance);
                        System.out.println("New Account Balance:" + "£" + newBalance);
                    } catch (Exception e) {
                        isItAFloat = false;
                        System.out.println("\nThis is not a correct value input");
                    }
                    System.out.print("\nIs this correct (Y/N)?: ");
                    response = sc.nextLine().toUpperCase();
                }
            } while (!response.equals("Y"));
            System.out.println("New Account Balance:" + "£" + userOpenBalance);
            Integer.valueOf(userOpenBalance);
            return userOpenBalance;
        }

    }

     class NewUserSecretQuestion {
        // set conditional
        String secretQuestionAnswer;
        Scanner sc = new Scanner(System.in);
        String response;

        public String secretQuestion() {
            System.out.println(" ");
            System.out.println("\nPlease now answer the following secret question");
            System.out.println("\nWhat was your first pets name?: ");
            secretQuestionAnswer = sc.nextLine();
            System.out.println("The secret answer is set to: " + " " + secretQuestionAnswer);


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
    }

    class CheckNotARobot {
        int answer;
        Integer userAnswer;
        Scanner sc = new Scanner(System.in);


        public boolean checkHuman() {
            //No condition or iterations as checks once to ensure the user is human.
            //Generates a random number between 1-10 for each multiplication value.

            int min = 1;
            int max = 10;
            int number1 = (int) Math.floor(Math.random() * (max - min + 1) + min);
            int number2 = (int) Math.floor(Math.random() * (max - min + 1) + min);
            int answerToQuestion = number1 * number2;
            System.out.println("\nPlease now complete the following question");
            System.out.println("What is" + " " + number1 + "x" + " " + number2 + "" + "= ");
            userAnswer = sc.nextInt();
            // Checks to see if user answer is equal to 30.
            if (userAnswer.equals(answerToQuestion)) {
                System.out.println("\nNew User passed security check");
                System.out.println("\nUser now registered");
                System.out.println("\nYou can now log in to your NewBank account");
                return true;
            }
            System.out.println("\nYou have failed the security check, and cannot register an account with us today");
            return false;
        }
    }

