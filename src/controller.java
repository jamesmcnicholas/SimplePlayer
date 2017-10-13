import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class controller {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;

    @FXML protected void loginButtonPress(ActionEvent event){
        System.out.println("login PRESSED");
    }
    @FXML protected void forgotPasswordButtonPress(ActionEvent event){
        System.out.println("forgot PRESSED");
    }
    @FXML protected void createAccountButtonPress(ActionEvent event){
        System.out.println("accout PRESSED");
    }
}
