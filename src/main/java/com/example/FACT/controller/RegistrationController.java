package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.IUserDAO;
import com.example.FACT.model.SqliteUserDAO;
import com.example.FACT.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * A controller class handling methods related to user registration.
 */
public class RegistrationController implements Initializable {

    @FXML
    private ImageView brandingImageView;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Button closeButton;
    @FXML
    private Label registerMessageLabel;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    public IUserDAO model = new SqliteUserDAO();
    private String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingfile = new File("images/logo.png");
        Image brandingImage = new Image(brandingfile.toURI().toString());
        brandingImageView.setImage(brandingImage);

    }

    /**
     * loads the login page
     */
    public void loginButtonOnAction(){
        loadLogin();
    }

    /**
     * validates user data and returns relevant message
     * @param event click of the signup button
     */
    public void signUpButtonOnAction(ActionEvent event){
        registerMessageLabel.setText("");
        confirmPasswordLabel.setText("");
        if(!emailTextField.getText().isBlank() && !setPasswordField.getText().isBlank() && !firstNameTextField.getText().isBlank() && !lastNameTextField.getText().isBlank()){
            if(patternMatches(emailTextField.getText(), regex)){
                if (setPasswordField.getText().equals(confirmPasswordField.getText())) {
                    registerUser();
                } else {
                    confirmPasswordLabel.setText("Passwords do not match");
                }
            }
            else{
                registerMessageLabel.setText("Please enter a valid email");
            }

        }
        else{
            registerMessageLabel.setText("Please enter all fields");
        }
    }
    public static boolean patternMatches(String emailAddress, String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(emailAddress).matches();
    }
    /**
     * method to close the application
     */
    public void closeButtonAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * creates a new user object from the input data and calls addUser to validate
     */
    public void registerUser(){
        String firstname = firstNameTextField.getText();
        String lastname = lastNameTextField.getText();
        String email = emailTextField.getText();
        String password = setPasswordField.getText();
        User user = new User(firstname, lastname, email, password);
        try{
            if (model.addUser(user)) {
                registerMessageLabel.setText("Sign up successful");
            }
            else{
                registerMessageLabel.setText("Please ensure all details are valid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * closes the current stage and opens a new one with the login content
     */
    public void loadLogin(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
            currentStage.hide();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }
}
