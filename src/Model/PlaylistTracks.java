package Model;

public class PlaylistTracks {
    private int playlistID;
    private int trackID;

    public PlaylistTracks(int playlistID, int trackID) {
        this.playlistID = playlistID;
        this.trackID = trackID;
    }

    //Getters and Setters
    public int getPlaylistID() { return playlistID; }
    public void setPlaylistID(int playlistID) { this.playlistID = playlistID; }

    public int getTrackID() { return trackID; }
    public void setTrackID(int trackID) { this.trackID = trackID; }
}
