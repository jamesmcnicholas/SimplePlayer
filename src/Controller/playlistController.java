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
    @FXML private Label yourPlaylistsLabel;

    private ObservableList<String> playlistNames = FXCollections.observableArrayList();
    private DatabaseConnection database = Main.database;
    private SongView song = mainController.selectedSong;

    public void initialize(){

        songNameLabel.setText("Selected song: "+ song.getName());

        PlaylistsService.selectAll(playlistList,database);
    }

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
        Playlists playlist = new Playlists(newPlaylist,1);
        try{
                PlaylistsService.save(playlist, database);
                updateListView();

        }catch (NullPointerException n){
            System.out.println(n.getMessage() + n.getCause());
        }
    }

    @FXML void removeSongButtonPressed(ActionEvent event) {

    }

    private void updateListView(){
        PlaylistsService.selectByUserID(playlistList,currentUser.getUserID(),database);
        for(Playlists p :playlistList){
            playlistNames.add(p.getPlaylistName());
        }
        playlistsListView.setItems(playlistNames);
    }


}

