package Model;

public class UserData {
    //Private declarations to prevent the attributes from being accessed outside the getters and setters
    private int userID;
    private String username;
    private String password;
    private int accessLevel;

    //Generic constructor for creating new instances of this object
    public UserData(int userID, String username,  String password, int accessLevel){
        this.userID = userID;
        this.username = username;
        this. password = password;
        this.accessLevel = accessLevel;
    }

    //Overloaded method for creating Users without the userID
    public UserData(String username, String password, int accessLevel) {
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    //Getters and Setters
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAccessLevel() { return accessLevel; }
    public void setAccessLevel(int accessLevel) { this.accessLevel = accessLevel; }

    //Overrides the toString() method, which is called if an instance of the object is printed
    @Override
    public String toString() {
        return "User:{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accessLevel=" + accessLevel +
                '}';
    }
}
