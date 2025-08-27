package com.example.FACT;

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
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {

    @FXML
    private ImageView brandingImageView;
    @FXML
    private Button loginButton;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingfile = new File("images/logo.png");
        Image brandingImage = new Image(brandingfile.toURI().toString());
        brandingImageView.setImage(brandingImage);

    }
    public void loginButtonOnAction(){
        loadLogin();
    }
    public void signUpButtonOnAction(ActionEvent event){
        if(setPasswordField.getText().equals(confirmPasswordField.getText())){
            registerUser();

        }
        else{
            confirmPasswordLabel.setText("Passwords do not match");
        }
    }

    public void registerUser(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        String firstname = firstNameTextField.getText();
        String lastname = lastNameTextField.getText();
        String email = emailTextField.getText();
        String password = setPasswordField.getText();

        String insertFields = "INSERT INTO user_account(first_name, last_name, email, password) VALUES ('";
        String insertValues = firstname + "','"+ lastname + "','"+ email + "','"+ password + "')";
        String insertUser = insertFields + insertValues;
        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertUser);
            registerMessageLabel.setText("Sign up successful");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

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
