package Model;

public class Playlists {
    private int playlistID;
    private int playlistLength;
    private String playlistName;
    private int userID;

    //Getters and Setters
    public int getPlaylistID() { return playlistID; }
    public void setPlaylistID(int playlistID) { this.playlistID = playlistID; }

    public int getPlaylistLength() { return playlistLength; }
    public void setPlaylistLength(int playlistLength) { this.playlistLength = playlistLength; }

    public String getPlaylistName() { return playlistName; }
    public void setPlaylistName(String playlistName) { this.playlistName = playlistName; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
}