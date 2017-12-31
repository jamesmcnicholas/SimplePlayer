package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static Model.DatabaseConnection.deleteFromTable;

public class ArtistDataService {

    public static void selectAll(List<ArtistData> targetList,DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT artistID, artistName FROM ArtistData ORDER BY artistID");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results!=null){
                    while(results.next()){
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
