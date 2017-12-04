import Model.DatabaseConnection;
import Model.TrackData;
import Model.UserData;
import Model.UserDataService;
import Model.TrackDataService;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


public class Main extends Application{


    public static void main(String args[]){launch(args);}

    public static DatabaseConnection database;

    @Override
    public void start(Stage stage)throws Exception{
        database = new DatabaseConnection("SQL/SimplePlayer.db");

        Parent root = FXMLLoader.load(getClass().getResource("Controller/LoginScreen.fxml"));
        Scene scene = new Scene(root,350,200);
        stage.setTitle("Login Screen");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(new Image("sp-logo.png"));

        stage.show();

        //TEST CODE START
        //UserData Table Test
        System.out.println("Users: ");
        ArrayList<UserData> userList = new ArrayList<>();
        UserDataService.selectAll(userList, database);
        //returns the details of every user in the database
        for (UserData u:userList){
            System.out.println(u.getUserID()+", Username: "+u.getUsername()+", Password: "+u.getPassword()+", Access Level: "+u.getAccessLevel());
        }
        System.out.println("");

        //TrackData Table Test
        System.out.println("Tracks: ");
        ArrayList<TrackData> trackList = new ArrayList<>();
        TrackDataService.selectAll(trackList, database);
        //returns the details of every song in the database
        for (TrackData t:trackList){
            System.out.println(t.getTrackID()+", Title: "+t.getTrackName()+", Length: "+t.getLength()+", Artist ID: "+t.getArtistID());
        }
        System.out.println("");



        //TEST CODE END

    }

}
