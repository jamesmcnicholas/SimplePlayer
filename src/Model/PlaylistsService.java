package Model;

import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Model.DatabaseConnection.deleteFromTable;

public class PlaylistsService {

    public static void selectAll(List<Playlists> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT PlaylistID, playlistName, PlaylistLength, userID FROM Playlists ORDER BY PlaylistID");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results!=null){
                    while(results.next()){
                        targetList.add(new Playlists(
                                results.getInt("PlaylistID"),
                                results.getInt("PlaylistLength"),
                                results.getString("playlistName"),
                                results.getInt("userID")
                        ));
                    }
                }
            }
        } catch(SQLException resultsException){
            System.out.println("Database select all error: "+resultsException.getMessage());
        }
    }
    
    
    public static Playlists selectByID(int id, DatabaseConnection database){
        Playlists result = null;
        PreparedStatement statement = database.newStatement("SELECTPlaylistID, playlistName, PlaylistLength, userID FROM Playlists WHERE id = ?");

        try {
            if (statement != null) {

                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    result = new Playlists(
                            results.getInt("PlaylistID"),
                            results.getInt("PlaylistLength"),
                            results.getString("playlistName"),
                            results.getInt("userID"));
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }
        return result;
    }

    public static Playlists selectByUserID(ObservableList<Playlists> targetList, int id, DatabaseConnection database){
        Playlists result = null;
        PreparedStatement statement = database.newStatement("SELECTPlaylistID, playlistName, PlaylistLength, userID FROM Playlists WHERE userID = ?");

        try {
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if(results!=null){
                    while(results.next()){
                        targetList.add(new Playlists(
                                results.getInt("PlaylistID"),
                                results.getInt("PlaylistLength"),
                                results.getString("playlistName"),
                                results.getInt("userID")
                        ));
                    }
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }
        return result;
    }


    public static void save(Playlists itemToSave, DatabaseConnection database){
        Playlists existingItem = null;
        if (itemToSave.getPlaylistID() != 0) existingItem = selectByID(itemToSave.getPlaylistID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO Playlists (PlaylistID, playlistName, PlaylistLength, userID) VALUES (?, ?, ?, ?)");
                statement.setInt(1, itemToSave.getPlaylistID());
                statement.setString(2, itemToSave.getPlaylistName());
                statement.setInt(3,itemToSave.getPlaylistLength());
                statement.setInt(4,itemToSave.getUserID());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE Playlists SET PlaylistID, playlistName, PlaylistLength, userID = ?, WHERE PlaylistID = ?");
                statement.setInt(1, itemToSave.getPlaylistID());
                statement.setString(2, itemToSave.getPlaylistName());
                statement.setInt(3,itemToSave.getPlaylistLength());
                statement.setInt(4,itemToSave.getUserID());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteByID(int id, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("DELETE FROM Playlists WHERE PlaylistID = ?");
        deleteFromTable(id,database,statement);
    }
}
