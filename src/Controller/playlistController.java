package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;


public class playlistController {

    //Variable declarations
    private ObservableList<Playlists> playlistList;
    private UserData currentUser;

    //FXML element declarations
    @FXML private ListView<String> playlistsListView;
    @FXML private TextField playlistNameField;
    @FXML private Button newPlaylistButton;
    @FXML private Button addSongButton;
    @FXML private Button removeSongButton;
    @FXML private Button deletePlaytlistButton;
    @FXML private Button backButton;
    @FXML private Label songNameLabel;
    private ObservableList<String> playlistNames = FXCollections.observableArrayList();
    public DatabaseConnection database;

    //Button methods
    @FXML void addSongButtonPressed(ActionEvent event) {}
    @FXML void backButtonPressed(ActionEvent event) {
        //Gets a handle to the stage
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
    @FXML void deletePlaylistButtonPressed(ActionEvent event) {

    }

    @FXML protected void newPlaylistButtonPressed(ActionEvent event) {
        String newPlaylist = playlistNameField.getText();

        System.out.println("database:"+ this.database);
        Playlists playlist = new Playlists("Test",1);
        System.out.println(playlist);
        PlaylistsService.selectAll(playlistList,database);
        if (!playlistList.contains(playlist)) {
            PlaylistsService.save(playlist, database);
            updateListView();
        }
    }

    @FXML void removeSongButtonPressed(ActionEvent event) {

    }

    public void initData(SongView song, UserData user,DatabaseConnection database){
        this.currentUser=user;
        this.database=database;

        if(song==null || song.getName()==null || song.getArtist()==null){
            System.out.println("broken");
        }else{
            songNameLabel.setText("Selected: "+song.getName()+" - "+song.getArtist());
        }
    }

    private void updateListView(){
        PlaylistsService.selectByUserID(playlistList,currentUser.getUserID(),database);
        for(Playlists p :playlistList){
            playlistNames.add(p.getPlaylistName());
        }
        playlistsListView.setItems(playlistNames);
    }


}

