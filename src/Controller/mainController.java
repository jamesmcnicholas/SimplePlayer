package Controller;

import Model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class mainController{

    //Variable declarations relating to the media player
    private MediaPlayer player;
    private boolean paused=false;
    private boolean playerInitialised;
    private boolean loop;
    private boolean shuffle;
    private TrackData currentSong;
    private ArrayList<TrackData> trackList = new ArrayList<>();
    private double currentVolume = 100;
    private ArrayList<SongView> queue = new ArrayList<>();

    //Other declarations
    private DatabaseConnection database;
    private ObservableList<SongView> tableSongs = FXCollections.observableArrayList();
    private UserData user;
    File cwd = new File("Songs/").getAbsoluteFile();


    //FXML TableView declarations
    @FXML private TableView<SongView> tableView;
    @FXML private TableColumn<SongView,SimpleStringProperty> NameColumn;
    @FXML private TableColumn<SongView,SimpleStringProperty> ArtistColumn;
    @FXML private TableColumn<SongView,SimpleStringProperty> LengthColumn;

    //Other FXML Declarations
    @FXML private Label volumeLabel;
    @FXML private Label nameDisplayLabel;
    @FXML private Slider progressBar;
    @FXML private Slider volumeSlider;
    @FXML private Label currentSongText;
    @FXML private Label currentTimeLabel;
    @FXML private Label lengthLabel;

    //Method for initialising the database and other items
    public void initData(UserData user,DatabaseConnection d) {
        this.user = user;
        nameDisplayLabel.setText("Music Library - " + this.user.getUsername());

        database = d;
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(100);
        volumeLabel.setText("VOLUME: 100%");
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            volumeLabel.setText("VOLUME: " + newValue.intValue() + "%");
            player.setVolume(newValue.doubleValue() / 100);
            currentVolume = (newValue.doubleValue() / 100);
        });

        progressBar.setMin(0);

        //Sets up columns
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        ArtistColumn.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        LengthColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));

        //Load dummy data
        tableView.setItems(initialiseTable());

        tableView.setRowFactory( tv -> {
            TableRow<SongView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    SongView rowData = row.getItem();
                    playRow(rowData);
                }
            });
            return row ;
        });


    }


    //Methods for button handling
    @FXML protected void toggleLoop(ActionEvent event){ loop = !loop; }
    @FXML protected void toggleShuffle(ActionEvent event){ shuffle = !shuffle; }
    @FXML protected void nextButtonPressed(ActionEvent event){ playNext(); }
    @FXML protected void prevButtonPressed(ActionEvent event){
        if(player.getCurrentTime().toSeconds()>3){
          player.seek(Duration.seconds(0));
        }else{
          playLast();
        }
    }
    @FXML protected void pauseButtonPressed() {
        try {
            if (playerInitialised && currentSong.getTrackName().equals(getSelectedRow().getName())) {
                if (!paused) {
                    player.pause();
                    System.out.println("Paused");
                    paused = true;

                } else {
                    player.play();
                    System.out.println("Playing");
                    paused = false;
                }
            } else {
                playRow(getSelectedRow());
            }
        }catch (NullPointerException n){
            System.out.println(n.getMessage());
        }
    }
    @FXML protected void queueButtonPressed(ActionEvent event) {
        if (!queue.contains(getSelectedRow())) {
            queue.add(getSelectedRow());
            System.out.println("Song queued");
        }else{
            System.out.println("Song already queued");
        }
    }

    @FXML protected void playNextButtonPressed(ActionEvent event){
        if(queue.contains(getSelectedRow())){
            queue.remove(getSelectedRow());
        }
        queue.add(0,getSelectedRow());
        System.out.println("Song moved to start of queue");
    }

    @FXML protected void addSongButtonPressed(ActionEvent event) {
        //Opens file explorer and gets PATH to music
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        configureFileChooser(fileChooser);

        List<File> filesToAdd = fileChooser.showOpenMultipleDialog(stage);
        if(filesToAdd != null){
            for(File f: filesToAdd){
                try {
                    copy(f,cwd);
                    File copiedSong = FileUtils.getFile("Songs",f.getName());
                    addSongToDatabase(getMetadata(copiedSong));
                    System.out.println("Success!");
                    addToTable(getMetadata(f));
                    tableView.refresh();
                }catch (Exception e){
                    System.out.println("Adding failed");
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    @FXML protected void removeSongButtonPressed(ActionEvent event){
        SongView row = getSelectedRow();
        if(row!=null){
            TrackData track = getTrackFromName(row.getName());
            System.out.println(track.getTrackName());


            System.out.println(track.getTrackID());
            TrackDataService.deleteByID(track.getTrackID(),database);

            tableSongs.remove(row);

            try {
                FileUtils.forceDelete(FileUtils.getFile(track.getPath()));
            }catch (IOException i){
                System.out.println(i.getMessage());
            }
        }else{
            System.out.println("Select a song first");
        }
    }

    @FXML protected void addToPlaylistButtonPressed(ActionEvent event) throws IOException{
        SongView song = tableView.getSelectionModel().getSelectedItem();
        //Creates a new FXMLLoader object and loads in the main controller
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("playlistScreen.fxml"));
        playlistController controller = new playlistController();
        controller.initData(song,user,database);
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }


    //Getters for database items
    private String getArtistName(int id){
        try{
            return ArtistDataService.selectByID(id,database).getArtistName();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    private void addSongToDatabase(TrackData track){
        TrackDataService.selectAll(trackList, database);
        boolean trackExists = false;
        for (TrackData t : trackList) {
            if ((t.getPath().equals(track.getPath()))) {
                trackExists=true;
                break;
            }
        }
        if(!trackExists) {
            TrackDataService.save(track,database);
            System.out.println("Track successfully added!");
        }else{
            System.out.println("Track is already in library");
        }
    }
    private int getArtistID(String artist) {
        if (artist != null) {
            ArrayList<ArtistData> artistList = new ArrayList<>();
            ArtistDataService.selectAll(artistList, database);
            for (ArtistData a : artistList) {
                if (artist.equals(a.getArtistName())) {
                    return a.getArtistID();
                }
            }
            ArtistData newArtist = new ArtistData((artist));
            ArtistDataService.save(newArtist, database);
            System.out.println("Artist not found, adding to database");
            return getArtistID(artist);
        }
        return 0;
    }
    @Nullable
    private TrackData getMetadata(File file) throws Exception{
        String fileLocation = file.getAbsolutePath();
        try {

            InputStream input = new FileInputStream(new File(fileLocation));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();
            int artistID = 0;
            if(metadata.get("xmpDM:artist")!=null) {
                artistID = getArtistID(metadata.get("xmpDM:artist"));
            }
            int duration = convertToSeconds(Double.parseDouble(metadata.get(XMPDM.DURATION)));
            if(metadata.get("title")!=null){
                String title = metadata.get("title");
                return new TrackData(title,duration,artistID,("Songs/"+file.getName()));
            }else{
                String title = file.getName();
                return new TrackData(title,duration,artistID,("Songs/"+file.getName()));
            }


        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SAXException e) {
            System.out.println(e.getMessage());
        } catch (TikaException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    @Nullable
    private String getPath(String name){
        ArrayList<TrackData> tracks = new ArrayList<>();
        TrackDataService.selectAll(tracks,database);
        for(TrackData t:tracks){
            if (t.getTrackName().equals(name)){
                return t.getPath();
            }
        }
        return null;
    }
    private TrackData getTrackFromName(String trackName) {
        if (trackName != null) {
            ArrayList<TrackData> trackList = new ArrayList<>();
            TrackDataService.selectAll(trackList, database);
            for (TrackData t : trackList) {
                if (trackName.equals(t.getTrackName())) {
                    System.out.println("Track id: "+t.getTrackID());
                    return t;
                }
            }
        }
        return null;
    }

    //File handling methods
    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("View Music");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Music Files", "*.*"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV", "*.wav")
        );
    }
    private static void copy(File source, File cwd) throws IOException {
        FileUtils.copyFileToDirectory(source,cwd);
    }
    private void loadIntoPlayer(File f){
        if(playerInitialised){
            player.stop();
        }

        try {
            TrackData playing = getMetadata(f);
            try {
                String artistName = ArtistDataService.selectByID(playing.getArtistID(), database).getArtistName();
                if(artistName==null){
                    artistName = "";
                }
                if(playing.getTrackName()==null){
                    currentSongText.setText("Now Playing: "+artistName+" - "+f.getName());
                }
                currentSongText.setText("Now Playing: "+artistName+" - "+playing.getTrackName());
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            Media pick = new Media(f.toURI().toURL().toString());
            player = new MediaPlayer(pick);
            currentSong = playing;
            player.setVolume(currentVolume);

            player.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                if(!paused) {
                    progressBar.setValue(newValue.toSeconds());
                }
                currentTimeLabel.setText(formatTime((int)Math.round(newValue.toSeconds())));
            });

            progressBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                if((((oldValue.intValue()+1)<(newValue.intValue()) || ((oldValue.intValue()-1)>(newValue.intValue()))))){
                    player.seek(Duration.seconds(newValue.intValue()));
                }
            });
            playerInitialised = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //Plays the next song when current one ends
        player.setOnEndOfMedia(() -> playNext());

        player.setOnReady(() -> {
            progressBar.setMax(player.getTotalDuration().toSeconds());
            lengthLabel.setText(formatTime((int)(Math.round(player.getTotalDuration().toSeconds()))));
        });

    }

    //Time formatting methods
    private int convertToSeconds(double milli){
        return (int)(milli/1000);
    }
    private String formatTime(int time){
        String formattedTime;
        if((time%60)<10){ formattedTime=((time /60)+":0"+(time%60));

        }else{ formattedTime=((time /60)+":"+(time%60)); }

        return formattedTime;
    }

    //TableView methods
    private ObservableList<SongView> initialiseTable(){
        TrackDataService.selectAll(trackList, database);

        for(TrackData t:trackList){
            String artist = getArtistName(t.getArtistID());
            SongView newRow = new SongView(t.getTrackName(),artist,formatTime(t.getLength()));
            tableSongs.add(newRow);
        }
        return tableSongs;
    }
    private ObservableList<SongView> addToTable(TrackData track){
        tableSongs.add(new SongView(track.getTrackName(),getArtistName(track.getArtistID()),formatTime(track.getLength())));
        return tableSongs;
    }
    @Nullable
    private SongView getSelectedRow(){
        if(tableView.getSelectionModel().getSelectedItem()!=null) {
            SongView newSong = tableView.getSelectionModel().getSelectedItem();
            return (newSong);
        }else{
            return null;
        }
    }
    private void playRow(SongView rowData){
        loadIntoPlayer(new File(getPath(rowData.getName())));
        player.play();
        paused = false;
    }
    private void playNext(){
        //Plays the next item in the queue then removes it if the queue is not empty
        if(queue.size()!=0){
            playRow(queue.get(0));
            queue.remove(0);
        }else {
            int id=0;
            if (!loop && !shuffle) {
                //If not looping or shuffling, get the next song and play it
                id = tableView.getSelectionModel().getSelectedIndex();
                if((id+1<tableView.getItems().size())){
                    tableView.getSelectionModel().select(id + 1);
                } else{
                    // If the item is out of range, resets it to the first
                    tableView.getSelectionModel().select(0);
                }
            } else if (loop) {
                //If loop enabled, reselect the current row and play again
                id = tableView.getSelectionModel().getSelectedIndex();
            } else {
                // Shuffle is chosen, picks a random song
                id = randomBetween(0,tableView.getItems().size());
                tableView.getSelectionModel().select(id);
            }
            // Finally plays the selected id
            playRow(tableView.getItems().get(id));
        }
    }

    private void playLast(){
        int id = tableView.getSelectionModel().getSelectedIndex();
        SongView rowData = tableView.getItems().get(id-1);
        playRow(rowData);
        tableView.getSelectionModel().select(rowData);
    }

    private int randomBetween(int min, int max){
            int range = (max - min) + 1;
            return (int)(Math.random() * range) + min;
    }


}