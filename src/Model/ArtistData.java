package Model;

public class ArtistData {
    //ID for each artist record
    private int artistID;
    //Variable(s) to store each column
    private String artistName;

    //Constructor, accepting parameters for each attribute
    public ArtistData(int artistID, String artistName) {
        this.artistID = artistID;
        this.artistName = artistName;
    }

    //Constructor with overloading to allow creation without an id
    public ArtistData(String artistName) {
        this.artistName = artistName;
    }

    //Getters and Setters
    public int getArtistID() { return artistID; }
    public void setArtistID(int artistID) { this.artistID = artistID; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
}
