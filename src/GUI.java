import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


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
}
