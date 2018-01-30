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

    //Initialising variables
    private DatabaseConnection database;
    private ArrayList<UserData> userList = new ArrayList<>();

    //FXML element declarations
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    //FXML methods for buttons
    @FXML protected void forgotPasswordButtonPress(ActionEvent event){
        System.out.println("forgot PRESSED");
    }
    @FXML protected void createAccountButtonPress(ActionEvent event) {
        //Selects all users from the database
        UserDataService.selectAll(userList, database);
        boolean accountExists = false;
        //Cycles through each user and checks if the given username exists
        for (UserData u : userList) {
            if ((u.getUsername().equals(usernameField.getText()))) {
                accountExists=true;
                break;
            }
        }
        //If the user does not exist, a new one is created
        if(!accountExists) {
            //The chosen password is hashed, and this is saved with the username to the database
            String passHash = generateHash(passwordField.getText());
            UserData newUser = new UserData(usernameField.getText(), passHash, 1);
            UserDataService.save(newUser, database);
            System.out.println("Account Created, try logging in!");
        }else{
            //If the account exists, the user is prompted to log in
            System.out.println("User exists, try logging in!");
        }
    }

    //Method to change the scene to the main screen
    @FXML public void loginButtonPushed(ActionEvent event) throws IOException {
        //All users are selected from the database
        UserDataService.selectAll(userList, database);
        boolean loggedIn = false;
        boolean invalidPass = false;

        //Cycles through every registered user
        for (UserData u:userList) {
            if (usernameField.getText().equals(u.getUsername())) {
                //The given password is hashed and checked against the stored hash
                if (generateHash(passwordField.getText()).equals(u.getPassword())) {
                    //If they match, the user is logged in
                    login(event, u, database);
                    loggedIn = true;
                    invalidPass = false;
                    break;
                } else {
                    invalidPass = true;
                }
            }
        }
        //If the  username or password is invalid, the user is notified, and may try to log in again
            if(invalidPass){
                System.out.println("Incorrect Password"); //todo.DEVELOP INTO DIALOG-POPUP
            } else if(!loggedIn) {
            System.out.println("Invalid username, try creating an account");
            }
        }

    //Method used for hashing passwords with the SHA-1 algorithm
    public static String generateHash(String text) {
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");
            hasher.update(text.getBytes());
            return DatatypeConverter.printHexBinary(hasher.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException nsae) {
            return nsae.getMessage();
        }
    }

    public void login(ActionEvent event,UserData u,DatabaseConnection d){
        try {
            //Creates a new FXMLLoader object and loads in the main controller
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("mainScreen.fxml"));
            Parent mainScreenParent = loader.load();

            //Sets up the scene with the elements from the FXML file
            Scene mainScreenScene = new Scene(mainScreenParent);

            //Runs the initial setup for the main screen, passing in the user and database connection
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
