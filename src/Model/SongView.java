package Model;

import javafx.beans.property.SimpleStringProperty;

public class SongView {

    private final SimpleStringProperty length;
    private final SimpleStringProperty name;
    private final SimpleStringProperty artist;

    public SongView(String name, String artist,String length){
        this.name=new SimpleStringProperty(name);
        this.artist=new SimpleStringProperty(artist);
        this.length= new SimpleStringProperty(length);
    }

    public String getName() { return name.get(); }
    public String getArtist() { return artist.get(); }
    public String getLength(){return length.get();}
}
