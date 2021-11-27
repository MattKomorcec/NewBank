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
import java.util.*;
import java.sql.SQLException;   // If have an error thrown from execution.

public class UserRegistration extends Main {


    public static UserRegistration newUser() throws SQLException {

        Scanner sc = new Scanner(System.in);
        // set all methods to return values - created conversions for sortcode
        // and account number to make it easier to push through to db.


        newUserName nn = new newUserName();
        String userFullname = nn.GetNameFromUser();


        DateChecks dc = new DateChecks();
        String userDoB = dc.GetDateFromUser();


        UserId ui = new UserId();
        String userUsername = ui.getNewUserId();


        newUserAccountNumber acm = new newUserAccountNumber();
        String userAccountNumber = acm.getNewUserAccountNumber();


        newUserSortcode nusort = new newUserSortcode();
        String userSortCode = nusort.getNewUserSortCode();


        NewUserSecurity nus = new NewUserSecurity();

        String userNewPass = nus.inputNewPassword();


        NewUserBalance nub = new NewUserBalance();

        Integer userNewBal = Integer.valueOf(nub.balance());


        newUserSecretQuestion nsq = new newUserSecretQuestion();

        String userNewSecretQuestion = nsq.secretQuestion();


        checkNotARobot cnr = new checkNotARobot();

        boolean NewUserNotARobot = cnr.checkHuman();

        // All already scanned by scanner then passed as values to 'String addNewUser'
        // The values are then passed to the db.


        Connection conn = null;

        try {
            String url = "jdbc:sqlite:/Users/pmcdowell/Documents/nb/database/bank.sq3";

            conn = DriverManager.getConnection(url);
            // Will allow for the database connection.

            String addNewUser = "INSERT INTO useroverview(name_user, dob_user, username_user, accountNumber_user, sortcode_user, password_user, balance_user, secretAns_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            // New values passed to db.
            PreparedStatement updateuserdb = conn.prepareStatement(addNewUser);

            updateuserdb.setString(1, userFullname);
            updateuserdb.setString(2, userDoB);
            updateuserdb.setString(3, userUsername);
            updateuserdb.setString(4, userAccountNumber);
            updateuserdb.setString(5, userSortCode);
            updateuserdb.setString(6, userNewPass);
            updateuserdb.setInt(7, userNewBal);
            updateuserdb.setString(8, userNewSecretQuestion);


            updateuserdb.executeUpdate();
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


    private static class newUserName {

        Scanner sc = new Scanner(System.in);
        String newName;

        public String GetNameFromUser() {

            System.out.println(" ");
            System.out.print("                                               Welcome to NewBank Registration");
            System.out.println(" ");
            System.out.println(" ");

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

        boolean IsItDate = false;
        String EnteredDate;
        String userdateresponse;

        public String GetDateFromUser() {

            do {
                System.out.print("\nFor your date of birth please enter in the format of (DD/MM/YYYY) including /");
                System.out.println("\nDate of Birth: ");
                String EnteredDate = sc.nextLine();
                System.out.println("\nThe date of birth is set to:" + " " + EnteredDate);
                IsItDate = IsDateValid(EnteredDate);
            } while (!IsItDate);

            do {
                System.out.println("Is the date of birth correct (Y/N)?: ");
                userdateresponse = sc.nextLine().toUpperCase();
                if (userdateresponse.equals("N")) {
                    System.out.print("\nPlease create a new Date of birth: ");
                    EnteredDate = sc.nextLine();

                    System.out.println("\nThe date of birth has been updated to:");
                    System.out.println("\nConfirm new date of birth(Y/N): " + " " + EnteredDate);
                    userdateresponse = sc.nextLine().toUpperCase();

                }
            } while (!userdateresponse.equals("Y"));


            return EnteredDate;
        }

        public boolean IsDateValid(String EnteredDate) {

            EnteredDate = EnteredDate + " 00:00:00";
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Date date;
            try {
                date = df.parse(EnteredDate);
            } catch (ParseException e) {
                System.out.println("Sorry invalid format date input please try again");
                return false;
            }

            return IsUserEighteen(date);
        }

        public boolean IsUserEighteen(Date dateToCheck) {

            Calendar DoB = Calendar.getInstance();
            DoB.setTime(dateToCheck);
            DoB.add(Calendar.YEAR, 18);
            Date newDate = DoB.getTime();
            Date dateToday = new Date();
            if (newDate.compareTo(dateToday) < 0) {
                return true;
            }
            System.out.println("\nThe data entered means you are less than 18.");
            return false;

        }

    }

    private static class UserId {

        Scanner sc = new Scanner(System.in);
        String userAns;
        String newUsername;

        public String getNewUserId() {

            do {
                System.out.print("\nPlease create a new username: ");
                newUsername = sc.nextLine();
                System.out.println("\nIs the username correct(Y/N): " + " " + newUsername);
                userAns = sc.nextLine().toUpperCase();

            } while (userAns.equals("N"));
            System.out.println("\n The username is set to:" + newUsername);
            return newUsername;
        }

    }


    private static class newUserAccountNumber {


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

    private static class newUserSortcode {

        public String getNewUserSortCode() {
            int min = 111212;
            int max = 998791;
            int accountSortCode = (int) Math.floor(Math.random() * (max - min + 1) + min);
            System.out.println("                 Sortcode:" + " " + accountSortCode);

            return String.valueOf(accountSortCode);
        }

    }


    private static class NewUserSecurity {

        Scanner sc = new Scanner(System.in);
        String newPassword;
        boolean passwordChecks;
        String userAnsPass;
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
                if (!passChecks(newPassword)) {
                    passwordChecks = false;
                }
            } while (!passwordChecks);
            System.out.println("\n Your new password is:" + " " + newPassword);

            do {
                System.out.println("\nConfirm password (Y/N): ");
                userAnsPass = sc.nextLine().toUpperCase();
                System.out.print("\nThe password must be (At least 8 letters long, contain a capital letter, a lowercase letter and numerical value)");
                System.out.println("\nPlease create your new password: ");
                newPassword = sc.nextLine();
                if (!passwordLength(newPassword)) {
                    passwordChecks = false;
                }
                if (!passChecks(newPassword)) {
                    passwordChecks = false;
                }
                System.out.println("\nConfirm new password(Y/N): " + " " + newPassword);
                userAnsPass = sc.nextLine().toUpperCase();
            } while (!passwordChecks && userAnsPass.equals("N"));

            System.out.println("\n Your new password is:" + " " + newPassword);
            return newPassword;
        }


        public boolean passwordLength(String newPassword) {
            if (newPassword.length() > 7) {
                return passChecks(newPassword);
            } else {
                System.out.println("\nSorry the password" + " " + newPassword + " " + "does not meet the requirements");
                return false;
            }
        }

        //Boolean password conditions = to check that:
        // Contains more than 8 chars.
        // Contains at least one numerical value.
        // Contains at least one capital letter.
        // Contains at least one lowercase letter.


        private boolean passChecks(String newPassword) {
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

    private static class NewUserBalance {

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

    private static class newUserSecretQuestion {
        // set conditional
        String quesAns;
        Scanner sc = new Scanner(System.in);
        String response;

        public String secretQuestion() {
            System.out.println(" ");
            System.out.println("\nPlease now answer the following secret question");
            System.out.println("\nWhat was your first pets name?: ");
            quesAns = sc.nextLine();
            System.out.println("The secret answer is set to: " + " " + quesAns);


            do {
                System.out.print("\nIs this correct (Y/N)?: ");
                response = sc.nextLine().toUpperCase();
                if (response.equals("N")) {
                    System.out.print("Ok, you would like to change the secret answer to?: ");
                    quesAns = sc.nextLine();
                    System.out.print("\nThe secret answer associated with this account will be:" + " " + quesAns);
                    System.out.print("\nIs this correct (Y/N)?: ");
                    response = sc.nextLine().toUpperCase();
                }
            } while (!response.equals("Y"));

            return quesAns;
        }
    }

    private static class checkNotARobot {
        Integer answer;
        Integer userAnswer;
        Scanner sc = new Scanner(System.in);


        public boolean checkHuman() {
            //No condition or iterations as checks once to ensure the user is human.
            answer = 30;
            System.out.println("\nPlease now complete the following question");
            System.out.println("\nWhat is 5 x 6 = "); // Answer is 30.
            userAnswer = sc.nextInt();
            // Checks to see if user answer is equal to 30.
            if (userAnswer.equals(answer)) {
                System.out.println("\nNew User passed security check");
                System.out.println("\nUser now registered");
                System.out.println("\nYou can now log in to your NewBank account");
                return true;
            }
            System.out.println("\nYou have failed the security check, and cannot register an account with us today");
            return false;
        }
    }


}