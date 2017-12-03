package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static Model.DatabaseConnection.deleteFromTable;

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
                PreparedStatement statement = database.newStatement("INSERT INTO UserData (username, password, accessLevel) VALUES (?, ?, ?)");
                statement.setString(1, itemToSave.getUsername());
                statement.setString(2,itemToSave.getPassword());
                statement.setInt(3,itemToSave.getAccessLevel());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE UserData SET username, password, accessLevel = ?, WHERE userID = ?");
                statement.setString(1, itemToSave.getUsername());
                statement.setString(2,itemToSave.getPassword());
                statement.setInt(3,itemToSave.getAccessLevel());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }


    public static void deleteByID(int id, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("DELETE FROM UserData WHERE UserID = ?");
        deleteFromTable(id,database,statement);
    }
}
