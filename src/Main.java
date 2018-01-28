import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application{


    public static void main(String args[]){launch(args);}


    @Override
    public void start(Stage stage)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Controller/LoginScreen.fxml"));
        Scene scene = new Scene(root,350,200);
        stage.setTitle("Login Screen");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(new Image("sp-logo.png"));

        stage.show();

    }

}
