package Model;

import javafx.beans.property.SimpleStringProperty;

public class SongView {
    private int id;
    private int length;
    private final SimpleStringProperty name;
    private final SimpleStringProperty artist;
    public SongView(int id,String name, String artist,int length){
        this.id=id;
        this.name=new SimpleStringProperty(name);
        this.artist=new SimpleStringProperty(artist);
        this.length=length;
    }

    public int getId() { return id; }
    public String getName() { return name.get(); }
    public String getArtist() { return artist.get(); }
    public int getLength(){return length;}
}
