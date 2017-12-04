package Controller;

import Model.DatabaseConnection;
import Model.UserData;
import Model.UserDataService;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class loginController {

    private DatabaseConnection database = new DatabaseConnection("SQL/SimplePlayer.db");
    ArrayList<UserData> userList = new ArrayList<>();

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML protected void forgotPasswordButtonPress(ActionEvent event){
        System.out.println("forgot PRESSED");
    }
    @FXML protected void createAccountButtonPress(ActionEvent event) {
        UserDataService.selectAll(userList, database);
        boolean accountExists = false;
        //if(userList.contains(usernameField.getText())){
        for (UserData u : userList) {
            if ((u.getUsername().equals(usernameField.getText()))) {
                accountExists=true;
                break;
            }
        }
        if(!accountExists) {
            String passHash = generateHash(passwordField.getText());
            UserData newUser = new UserData(usernameField.getText(), passHash, 1);
            UserDataService.save(newUser, database);
            System.out.println("Account Created, try logging in!");
        }else{
            System.out.println("User exists, try logging in!");
        }
    }

    //Method to change the scene to the main screen
    @FXML public void loginButtonPushed(ActionEvent event) throws IOException {

        UserDataService.selectAll(userList, database);
        boolean loggedIn = false;
        boolean invalidPass = false;

        //returns the details of every user in the database
        for (UserData u:userList) {
            if (usernameField.getText().equals(u.getUsername())) {
                if (generateHash(passwordField.getText()).equals(u.getPassword())) {
                    login(event, u);
                    loggedIn = true;
                    invalidPass = false;
                    break;
                } else {
                    invalidPass = true;
                }
            }
        }
            if(invalidPass){
                System.out.println("Incorrect Password"); //todo.DEVELOP INTO DIALOG-POPUP
            } else if(!loggedIn) {
            System.out.println("Invalid username, try creating an account");
            }
        }


    //Method used for hashing passwords with the SHA-1 algorithm
    public static String generateHash(String text)
    {
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-1");
            hasher.update(text.getBytes());
            return DatatypeConverter.printHexBinary(hasher.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException nsae) {
            return nsae.getMessage();
        }
    }

    public void login(ActionEvent event,UserData u){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("mainScreen.fxml"));
            Parent mainScreenParent = loader.load();

            Scene mainScreenScene = new Scene(mainScreenParent);
            mainController controller = loader.getController();
            controller.intitData(u);

            //This line gets the stage info
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(mainScreenScene);
            window.getIcons().add(new Image("sp-logo.png"));
            window.setTitle("Main Screen");
            window.show();
        }catch(IOException e){
            System.out.println("Error: "+e.getMessage()+e.getCause());
        }
    }
}
