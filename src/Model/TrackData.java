package Model;

public class TrackData {
    private int trackID;
    private String trackName;
    private int length;
    private int artistID;
    private String path;

    public TrackData(String trackName, int length, int artistID,String path) {
        this.trackName = trackName;
        this.length = length;
        this.artistID = artistID;
        this.path = path;
    }

    //Getters and Setters
    public int getTrackID() { return trackID; }
    public void setTrackID(int trackID) { this.trackID = trackID; }

    public String getTrackName() { return trackName; }
    public void setTrackName(String trackName) { this.trackName = trackName; }

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }

    public int getArtistID() { return artistID; }
    public void setArtistID(int artistID) { this.artistID = artistID; }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }
}
