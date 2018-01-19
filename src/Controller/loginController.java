package Controller;

import Model.*;
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

    private DatabaseConnection database;
    ArrayList<UserData> userList = new ArrayList<>();

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML protected void forgotPasswordButtonPress(ActionEvent event){
        System.out.println("forgot PRESSED");
    }
    @FXML protected void createAccountButtonPress(ActionEvent event) {
        UserDataService.selectAll(userList, database);
        boolean accountExists = false;
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
                    login(event, u, database);
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
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");
            hasher.update(text.getBytes());
            return DatatypeConverter.printHexBinary(hasher.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException nsae) {
            return nsae.getMessage();
        }
        //todo.Implement a salt for further security
    }

    public void login(ActionEvent event,UserData u,DatabaseConnection d){
        try {
            //Creates a new FXMLLoader object and loads in the main controller
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("mainScreen.fxml"));
            Parent mainScreenParent = loader.load();

            //Sets up the scene with the elements from the FXML file
            Scene mainScreenScene = new Scene(mainScreenParent);
            //Runs the initial setup for the main screen
            mainController controller = loader.getController();
            controller.initData(u,d);

            //Gets the stage info
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //Assigns details to the new scene (Icon, title) then shows the window.
            window.setScene(mainScreenScene);
            window.getIcons().add(new Image("sp-logo.png"));
            window.setTitle("SimplePlayer Library");
            window.show();
        }catch(IOException e){
            System.out.println("Error: "+e.getMessage()+e.getCause());
        }
    }

    public void initialize(){
        database = new DatabaseConnection("SQL/SimplePlayer.db");
    }
}
