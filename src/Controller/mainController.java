package Controller;

import Model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.media.Media;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


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
    @FXML protected void progressDragDropped(ActionEvent event) {
        System.out.println(event.toString());
        progressBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            player.seek(Duration.seconds(newValue.intValue()));
        });
    }

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

        List<File> filesToAdd = fileChooser.showOpenMultipleDialog(stage);
        if(filesToAdd != null){
            for(File f: filesToAdd){
                System.out.println(f);
                try {
                    copy(f,cwd);
                    getMetadata(f);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            loadIntoPlayer(filesToAdd.get(0));
        }
    }


    @FXML protected void removeSongButtonPressed(ActionEvent event){ System.out.println("song remove pressed"); }
    @FXML protected void playNextButtonPressed(ActionEvent event){ System.out.println("song will play next"); }
    @FXML protected void addToPlaylistButtonPressed(ActionEvent event){ System.out.println("added to playlist"); }

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

    public void loadIntoPlayer(File f){
        /**
         * This should be moved to a play method
         */
        currentSongText.setText("Now playing - " + f.getName());
        try {
            Media pick = new Media(f.toURI().toURL().toString());
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


    public void getMetadata(File file) throws Exception{
        String fileLocation = file.getAbsolutePath();
        try {

            InputStream input = new FileInputStream(new File(fileLocation));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();
             /*
             for(String name : metadataNames){
                  System.out.println(name + ": " + metadata.get(name));
             }
             */
            String[] metadataNames = metadata.names();

                // Retrieve the necessary info from metadata
                // Names - title, xmpDM:artist etc. - mentioned below may differ based
            System.out.println("----------------------------------------------");
            System.out.println("Title: " + metadata.get("title"));
            System.out.println("Artists: " + metadata.get("xmpDM:artist"));
            System.out.println("Composer : "+metadata.get("xmpDM:composer"));
            System.out.println("Genre : "+metadata.get("xmpDM:genre"));
            System.out.println("Album : "+metadata.get("xmpDM:album"));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (TikaException e) {
                e.printStackTrace();
            }
    }
}





