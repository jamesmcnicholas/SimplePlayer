import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
public class loginController {

    @FXML protected void loginButtonPress(ActionEvent event){ System.out.println("login pressed"); }
    @FXML protected void forgotPasswordButtonPress(ActionEvent event){
        System.out.println("forgot PRESSED");
    }
    @FXML protected void createAccountButtonPress(ActionEvent event){System.out.println("account PRESSED");}

    //Method to change the scene to the main screen
    public void changeSceneButtonPushed(ActionEvent event) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Scene mainScreenScene = new Scene(mainScreenParent);

        //This line gets the stage info
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(mainScreenScene);
        window.getIcons().add(new Image("sp-logo.png"));
        window.setTitle("Main Screen");
        window.show();
    }

}
