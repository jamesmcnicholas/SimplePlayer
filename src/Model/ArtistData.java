package Model;

public class ArtistData {
    private int artistID;
    private String artistName;

    public ArtistData(int artistID, String artistName) {
        this.artistID = artistID;
        this.artistName = artistName;
    }

    public ArtistData(String artistName) {
        this.artistName = artistName;
    }

    //Getters and Setters
    public int getArtistID() { return artistID; }
    public void setArtistID(int artistID) { this.artistID = artistID; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
}
