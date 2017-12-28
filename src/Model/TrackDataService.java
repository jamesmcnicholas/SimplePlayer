package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static Model.DatabaseConnection.deleteFromTable;

public class TrackDataService {
    public static void selectAll(List<TrackData> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT trackID, trackName, length, artistID FROM TrackData ORDER BY trackID");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results!=null){
                    while(results.next()){
                        targetList.add(new TrackData(
                                results.getString("trackName"),
                                results.getInt("length"),
                                results.getInt("artistID")
                        ));
                    }
                }
            }
        } catch(SQLException resultsException){
            System.out.println("Database select all error: "+resultsException.getMessage());
        }
    }


    public static TrackData selectByID(int id, DatabaseConnection database){
        TrackData result = null;
        PreparedStatement statement = database.newStatement("SELECT trackID, trackName, length, artistID FROM TrackData WHERE id = ?");

        try {
            if (statement != null) {

                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    result = new TrackData(
                            results.getString("trackName"),
                            results.getInt("length"),
                            results.getInt("artistID"));
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }
        return result;
    }
    public static void save(TrackData itemToSave, DatabaseConnection database){
        TrackData existingItem = null;
        if (itemToSave.getTrackID() != 0) existingItem = selectByID(itemToSave.getTrackID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO TrackData (trackID, trackName, length, artistID) VALUES (?, ?, ?, ?)");
                statement.setInt(1, itemToSave.getTrackID());
                statement.setString(2, itemToSave.getTrackName());
                statement.setInt(3,itemToSave.getLength());
                statement.setInt(4,itemToSave.getArtistID());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE TrackData SET trackID, trackName, length, artistID = ?, WHERE trackID = ?");
                statement.setInt(1, itemToSave.getTrackID());
                statement.setString(2, itemToSave.getTrackName());
                statement.setInt(3,itemToSave.getLength());
                statement.setInt(4,itemToSave.getArtistID());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }


    public static void deleteByID(int id, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("DELETE FROM TrackData WHERE trackID = ?");
        deleteFromTable(id,database,statement);
    }
}
