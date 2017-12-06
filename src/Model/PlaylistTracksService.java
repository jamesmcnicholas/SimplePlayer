package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static Model.DatabaseConnection.deleteFromTable;

public class PlaylistTracksService {
    
    public static void selectAll(List<PlaylistTracks> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT playlistID, trackID, FROM PlaylistTracks ORDER BY playlistID");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results!=null){
                    while(results.next()){
                        targetList.add(new PlaylistTracks(
                                results.getInt("playlistID"),
                                results.getInt("trackID")
                        ));
                    }
                }
            }
        } catch(SQLException resultsException){
            System.out.println("Database select all error: "+resultsException.getMessage());
        }
    }


    public static PlaylistTracks selectByID(int id, DatabaseConnection database){
        PlaylistTracks result = null;
        PreparedStatement statement = database.newStatement("SELECT playlistID, trackID, FROM PlaylistTracks WHERE id = ?");

        try {
            if (statement != null) {

                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    result = new PlaylistTracks(
                            results.getInt("playlistID"),
                            results.getInt("trackID"));
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }
        return result;
    }


    public static void save(PlaylistTracks itemToSave, DatabaseConnection database){
        PlaylistTracks existingItem = null;
        if (itemToSave.getPlaylistID()!= 0) existingItem = selectByID(itemToSave.getPlaylistID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO PlaylistTracks (playlistID, trackID) VALUES (?, ?)");
                statement.setInt(1, itemToSave.getPlaylistID());
                statement.setInt(2, itemToSave.getTrackID());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE PlaylistTracks SET playlistID, trackID,= ?, WHERE playlistID = ?");
                statement.setInt(1, itemToSave.getPlaylistID());
                statement.setInt(2, itemToSave.getTrackID());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteByID(int id, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("DELETE FROM PlaylistTracks WHERE playlistID = ?");
        deleteFromTable(id,database,statement);
    }
}
