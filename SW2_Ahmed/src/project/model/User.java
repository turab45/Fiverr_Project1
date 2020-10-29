package project.model;

public class User {
    private static String username;
    private static String password;
    private static int userID;

    //Default Constructor
    public User(){
        this(null, null, 0);
    }

    //Main Constructor
    public User(String username, String password, int userID){
        this.username = username;
        this.password = password;
        this.userID = userID;
    }

    public static String getUsername() {
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

    public static int getUserID() {
        return userID;
    }



    public void setUserID(int userID) {
        this.userID = userID;
    }
}
