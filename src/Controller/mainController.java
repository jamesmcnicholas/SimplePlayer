package Controller;

import Model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.media.Media;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.media.MediaPlayer;

import java.io.File;

public class mainController{

    private MediaPlayer player;
    private boolean paused;
    private DatabaseConnection database = new DatabaseConnection("SQL/SimplePlayer.db");
    private UserData user;


    @FXML private Label volumeLabel;
    @FXML private Label nameDisplayLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Slider volumeSlider;
    @FXML private Label lengthLabel;
    @FXML protected void nextButtonPressed(ActionEvent event){ System.out.println("skip pressed"); }
    @FXML protected void prevButtonPressed(ActionEvent event){ System.out.println("prev pressed"); }
    @FXML protected void  volumeSliderDragged(ActionEvent event) {
    }

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

    @FXML protected void addSongButtonPressed(ActionEvent event){
        //Opens file explorer and gets PATH to music
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file);


        //Loads file into MusicPlayer
        try{
            Media pick = new Media(file.toURI().toURL().toString());
            player = new MediaPlayer(pick);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


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
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                volumeLabel.setText("VOLUME: "+newValue.intValue()+"%");
                player.setVolume(newValue.doubleValue()/100);
                System.out.println("Volume set to: "+newValue.doubleValue()/100);
            }
        });
    }

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

    /*private static void getDurationWithMp3Spi(File file) throws UnsupportedAudioFileException, IOException {

        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
        if (fileFormat instanceof TAudioFileFormat) {
            Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
            String key = "duration";
            Long microseconds = (Long) properties.get(key);
            int mili = (int) (microseconds / 1000);
            int sec = (mili / 1000) % 60;
            int min = (mili / 1000) / 60;
            System.out.println("time = " + min + ":" + sec);
        } else {
            throw new UnsupportedAudioFileException();
        }


    }
     */
}




