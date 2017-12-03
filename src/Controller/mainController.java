package Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class mainController{

    private DatabaseConnection database = new DatabaseConnection("SQL/SimplePlayer.db");
    private UserData user;

    @FXML private Label nameDisplayLabel;
    @FXML protected void nextButtonPressed(ActionEvent event){ System.out.println("skip pressed"); }
    @FXML protected void prevButtonPressed(ActionEvent event){ System.out.println("prev pressed"); }
    @FXML protected void pauseButtonPressed(ActionEvent event){ System.out.println("pause pressed"); }
    @FXML protected void queueButtonPressed(ActionEvent event){ System.out.println("queued song"); }
    @FXML protected void addSongButtonPressed(ActionEvent event){ System.out.println("add song pressed"); }
    @FXML protected void removeSongButtonPressed(ActionEvent event){ System.out.println("song remove pressed"); }
    @FXML protected void playNextButtonPressed(ActionEvent event){ System.out.println("song will play next"); }
    @FXML protected void addToPlaylistButtonPressed(ActionEvent event){ System.out.println("added to playlist"); }

    @FXML private TableView<SongView> songTable;
    @FXML private TableColumn<SongView,String> NameColumn;
    @FXML private TableColumn<SongView,Integer> TrackNumberColumn;
    @FXML private TableColumn<SongView,String> ArtistColumn;
    @FXML private TableColumn<SongView,Integer> lengthColumn;

    public void intitData(UserData user){
        this.user = user;
        nameDisplayLabel.setText("Music Library - "+this.user.getUsername());
    }

    public void initialize(){
        /**
         * Implement other service classes before trying to populate the table view
         * todo.Populate table with user's library
         */
        //ArtistColumn.setCellValueFactory(new PropertyValueFactory<>("Hello there"));
    }
}
