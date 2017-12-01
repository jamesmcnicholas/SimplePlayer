package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDataService {
    public static void selectAll(List<UserData> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT userID, username, password, accessLevel FROM UserData ORDER BY userID");
        try{
            if(statement != null){
            ResultSet results = database.executeQuery(statement);

            if(results!=null){
                while(results.next()){
                    targetList.add(new UserData(
                            results.getInt("userID"),
                            results.getString("username"),
                            results.getString("password"),
                            results.getInt("accessLevel")
                            ));
                    }
                }
            }
        } catch(SQLException resultsException){
            System.out.println("Database select all error: "+resultsException.getMessage());
        }
    }


    public static UserData selectByID(int id, DatabaseConnection database){
        UserData result = null;

        PreparedStatement statement = database.newStatement("SELECT userID, username, password, FROM UserData WHERE id = ?");

        try {
            if (statement != null) {

                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    result = new UserData(
                            results.getInt("userID"),
                            results.getString("username"),
                            results.getString("password"),
                            results.getInt("accessLevel"));
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }
        return result;
    }


    public static void save(UserData itemToSave, DatabaseConnection database){
        UserData existingItem = null;
        if (itemToSave.getUserID() != 0) existingItem = selectByID(itemToSave.getUserID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO UserData (userID, username, password, accessLevel) VALUES (?, ?, ?, ?)");
                statement.setInt(1, itemToSave.getUserID());
                statement.setString(2, itemToSave.getUsername());
                statement.setString(3,itemToSave.getPassword());
                statement.setInt(4,itemToSave.getAccessLevel());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE UserID SET userID, username, password, accessLevel = ?, WHERE userID = ?");
                statement.setInt(1, itemToSave.getUserID());
                statement.setString(2, itemToSave.getUsername());
                statement.setString(3,itemToSave.getPassword());
                statement.setInt(4,itemToSave.getAccessLevel());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }


    public static void deeleteByID(int id, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("DELETE FROM UserData WHERE UserID = ?");

        try {
            if (statement != null) {
                statement.setInt(1, id);
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database deletion error: " + resultsException.getMessage());
        }
    }
}
