package Controller;

import Model.DatabaseConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;


public class Main extends Application{

    public static DatabaseConnection database = new DatabaseConnection("SQL/SimplePlayer.db");

    public static void main(String args[]){launch(args);}


    @Override
    public void start(Stage stage)throws Exception{
        try{
            URL url = new File("src/View/LoginScreen.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root,350,200);
            stage.setTitle("Login Screen");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image("sp-logo.png"));

            stage.show();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

}
