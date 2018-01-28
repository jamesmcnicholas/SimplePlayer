package Controller;


import Model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


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

    //Button methods
    @FXML void addSongButtonPressed(ActionEvent event) {}
    @FXML void backButtonPressed(ActionEvent event) {
        //Gets a handle to the stage
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
    @FXML void deletePlaylistButtonPressed(ActionEvent event) {

    }
    @FXML void newPlaylistButtonPressed(ActionEvent event) {
        String newPlaylist = playlistNameField.getText();
        if(newPlaylist!=null){
            Playlists playlist = (new Playlists(newPlaylist,currentUser.getUserID()));
            playlistsListView.getItems().add(playlist.getPlaylistName());

        }
    }
    @FXML void removeSongButtonPressed(ActionEvent event) {

    }

    public void initData(SongView song, UserData user,DatabaseConnection database){
        this.currentUser=user;
        System.out.println(user);
        if(song==null || song.getName()==null || song.getArtist()==null){
            System.out.println("broken");
            try{
                songNameLabel.setText("Selected: "+song.getName()+" - "+song.getArtist());
            }catch (Exception e){ System.out.println(e.getMessage()); }

            PlaylistsService.selectByUserID(playlistList,user.getUserID(),database);
            for(Playlists p :playlistList){
                playlistsListView.getItems().add(p.getPlaylistName());
            }
        }

    }
}
