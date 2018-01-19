package Model;

import java.sql.PreparedStatement; //Statements for SQLite
import java.sql.ResultSet; //Holds results from queries
import java.sql.SQLException; //Handles SQL based exceptions
import java.util.List; //Holds parsed in data

import static Model.DatabaseConnection.deleteFromTable;

public class ArtistDataService {
    //Method for selecting all items in the table
    public static void selectAll(List<ArtistData> targetList,DatabaseConnection database){
        //Builds a statement ready for execution
        PreparedStatement statement = database.newStatement("SELECT artistID, artistName FROM ArtistData ORDER BY artistID");
        try{
            if(statement != null){
                //Executes the statement if it exists
                ResultSet results = database.executeQuery(statement);

                if(results!=null){
                    while(results.next()){
                        //While there are more results, add these results to the specified list
                        targetList.add(new ArtistData(
                                results.getInt("artistID"),
                                results.getString("artistName")
                        ));
                    }
                }
            }
        } catch(SQLException resultsException){
            System.out.println("Database select all error: "+resultsException.getMessage());
        }
    }
    public static ArtistData selectByID(int id, DatabaseConnection database){
        ArtistData result = null;
        PreparedStatement statement = database.newStatement("SELECT artistID, artistName FROM ArtistData WHERE ArtistID = ?");

        try {
            if (statement != null) {

                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    result = new ArtistData(
                            results.getInt("artistID"),
                            results.getString("artistName"));

                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }
        return result;
    }


    public static void save(ArtistData itemToSave, DatabaseConnection database){
        ArtistData existingItem = null;
        if (itemToSave.getArtistID() != 0) existingItem = selectByID(itemToSave.getArtistID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO ArtistData (artistName) VALUES (?)");
                statement.setString(1, itemToSave.getArtistName());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE ArtistData SET artistID, artistName = ?, WHERE artistID = ?");
                statement.setString(1, itemToSave.getArtistName());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteByID(int id, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("DELETE FROM ArtistData WHERE UserID = ?");
        deleteFromTable(id,database,statement);
    }
}
