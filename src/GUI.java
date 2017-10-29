import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class GUI extends Application{
    @Override
    public void start(Stage stage)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        Scene scene = new Scene(root,350,200);
        stage.setTitle("Login Screen");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(new Image("sp-logo.png"));

        stage.show();
    }
    public static void main(String args[]){launch(args);}

    public void login(ActionEvent event) throws Exception{
        try {
            Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Main Screen");
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
