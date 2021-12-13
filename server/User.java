package newbank.server;

public abstract class User {

    private int id;
    private String dob;
    public String username;
    public String password;
    private String secretAnswer;
    private int AccountLocked;
    private String fullname;

    public User(int id, String dob, String username, String password, String secretAnswer,
                int AccountLocked, String fullname) {
        this.id = id;
        this.dob = dob;
        this.username = username;
        this.password = password;
        this.secretAnswer = secretAnswer;
        this.AccountLocked = AccountLocked;
        this.fullname = fullname;
    }

    public User() {
    }

    public int getUserId() {
        return id;
    }

    public void setUserId(int id) {
        this.id = id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public int getAccountLocked() {
        return AccountLocked;
    }

    public void setAccountLocked(int accountLocked) {
        this.AccountLocked = accountLocked;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullName) {
        this.fullname = fullName;
    }
}
