import Model.DatabaseConnection;
import Model.UserData;
import Model.UserDataService;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.Console;
import java.util.ArrayList;


public class GUI extends Application{
    public static DatabaseConnection database;

    @Override
    public void start(Stage stage)throws Exception{
        database = new DatabaseConnection("SQL/SimplePlayer.db");

        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        Scene scene = new Scene(root,350,200);
        stage.setTitle("Login Screen");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(new Image("sp-logo.png"));

        stage.show();

        //TEST CODE START
        ArrayList<UserData> testList = new ArrayList<>();
        UserDataService.selectAll(testList, database);
        for (UserData u:testList){
            System.out.println(u);
        }
        //TEST CODE END
    }
    public static void main(String args[]){launch(args);}
}
