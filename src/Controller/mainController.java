package Controller;

import Model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.media.Media;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.media.MediaPlayer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class mainController{

    private MediaPlayer player;
    private boolean paused;
    private DatabaseConnection database = new DatabaseConnection("SQL/SimplePlayer.db");
    private UserData user;
    File cwd = new File("Songs/").getAbsoluteFile();


    @FXML private Label volumeLabel;
    @FXML private Label nameDisplayLabel;
    @FXML private Slider progressBar;
    @FXML private Slider volumeSlider;
    @FXML private Label currentSongText;
    @FXML private Label currentTimeLabel;
    @FXML private Label lengthLabel;

    public void intitData(UserData user) {
        this.user = user;
        nameDisplayLabel.setText("Music Library - " + this.user.getUsername());


        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(100);
        volumeLabel.setText("VOLUME: 100%");
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            volumeLabel.setText("VOLUME: " + newValue.intValue() + "%");
            player.setVolume(newValue.doubleValue() / 100);
        });


        progressBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            player.setCycleCount(newValue.intValue() / 100);
        });

        progressBar.setMin(0);
    }

    @FXML protected void nextButtonPressed(ActionEvent event){ System.out.println("skip pressed"); }
    @FXML protected void prevButtonPressed(ActionEvent event){ System.out.println("prev pressed"); }

    @FXML protected void pauseButtonPressed(ActionEvent event){
        if(paused){
            player.play();
            System.out.println("Playing");
            paused = false;
        }else{
            player.pause();
            System.out.println("Paused");
            paused = true;
        }
    }

    @FXML protected void queueButtonPressed(ActionEvent event){ }

    @FXML protected void addSongButtonPressed(ActionEvent event) {
        //Opens file explorer and gets PATH to music
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file);

        try {
            System.out.println("Length of file: "+Files.getAttribute(file.toPath(),"Length"));

            copy(file,cwd);
            System.out.println("test");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        /**
         * This should be moved to a play method
         */
        currentSongText.setText("Now playing - " + file.getName());
        try {
            Media pick = new Media(file.toURI().toURL().toString());
            player = new MediaPlayer(pick);

            player.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                if(!paused) {
                    progressBar.setValue(newValue.toSeconds());
                }
                currentTimeLabel.setText(Long.toString(Math.round(newValue.toSeconds())));
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        player.setOnReady(() -> {
            progressBar.setMax(player.getTotalDuration().toSeconds());
            lengthLabel.setText(Long.toString(Math.round(player.getTotalDuration().toSeconds())));
            paused=true;
            //todo.implement scrubbing
        });
    }


    @FXML protected void removeSongButtonPressed(ActionEvent event){ System.out.println("song remove pressed"); }
    @FXML protected void playNextButtonPressed(ActionEvent event){ System.out.println("song will play next"); }
    @FXML protected void addToPlaylistButtonPressed(ActionEvent event){ System.out.println("added to playlist"); }

    @FXML private TableView<SongView> songTable;
    @FXML private TableColumn<SongView,String> NameColumn;
    @FXML private TableColumn<SongView,Integer> TrackNumberColumn;
    @FXML private TableColumn<SongView,String> ArtistColumn;
    @FXML private TableColumn<SongView,Integer> lengthColumn;



    public void initialize(){
        /*
            todo.Populate table with user's library
         */
        //ArtistColumn.setCellValueFactory(new PropertyValueFactory<>("Hello there"));
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("View Music");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Music Files", "*.*"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV", "*.wav")
        );
    }

    private static void addFile(File file){
        //TrackData newTrack = new TrackData();
        //file.getName();
        //todo.Find a way to get track metadata and push to db
    }


    public static void copy(File source, File cwd) throws IOException {
        FileUtils.copyFileToDirectory(source,cwd);
    }
}




