package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static Model.DatabaseConnection.deleteFromTable;

public class PlaylistTracksService {
    
    public static void selectAll(List<PlaylistTracks> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT playlistID, trackID FROM PlaylistTracks ORDER BY playlistID");
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
        PreparedStatement statement = database.newStatement("SELECT playlistID, trackID FROM PlaylistTracks WHERE playlistID = ?");
        try {
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if (results!=null) {
                    while (results.next()) {
                        result = new PlaylistTracks(
                                results.getInt("playlistID"),
                                results.getInt("trackID"));
                    }
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }
        return result;
    }


    public static void save(PlaylistTracks itemToSave, DatabaseConnection database){

        try {
                PreparedStatement statement = database.newStatement("INSERT INTO PlaylistTracks (playlistID, trackID) VALUES (?, ?)");
                statement.setInt(1, itemToSave.getPlaylistID());
                statement.setInt(2, itemToSave.getTrackID());
                database.executeUpdate(statement);

        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteByID(int id, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("DELETE FROM PlaylistTracks WHERE playlistID = ?");
        deleteFromTable(id,database,statement);
    }

    public static void deleteRow(PlaylistTracks itemToDelete, DatabaseConnection database){
        try{
            PreparedStatement statement = database.newStatement("DELETE FROM PlaylistTracks WHERE playlistID = ? AND trackID = ?");
            statement.setInt(1,itemToDelete.getPlaylistID());
            statement.setInt(2,itemToDelete.getTrackID());
        } catch (SQLException resultsException){
            System.out.println("Database deletion error: "+resultsException.getMessage());
        }
    }
}
