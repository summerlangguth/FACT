package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.SqliteUserDAO;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private Button loginButton;

    @FXML
    private Button signButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    public SqliteUserDAO model = new SqliteUserDAO();

    @FXML
    private Button homepagePracButton;

    @FXML
    private Button logoutButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingfile = new File("images/logo.png");
        Image brandingImage = new Image(brandingfile.toURI().toString());
        brandingImageView.setImage(brandingImage);

    }

    /**
     * creates the registration form
     * @param event signup button click
     */
    public void signButtonOnAction(ActionEvent event){
        createAccountForm();
    }

    /**
     * validates user credentials and calls isLogin to check.
     * @param event login bytton click
     */
    public void loginButtonOnAction(ActionEvent event){
        loginMessageLabel.setText("");
        if(!emailTextField.getText().isBlank() && !passwordField.getText().isBlank()){
            try{
                if(model.isLogin(emailTextField.getText(), passwordField.getText())){
                    //loginMessageLabel.setText("valid login");
                    loadHomePage();
                }
               else {
                    loginMessageLabel.setText("Email or password is incorrect");
                }
            }
            catch(SQLException e){
                loginMessageLabel.setText("Email or password is incorrect");
                e.printStackTrace();
            }

        }
        else{
            loginMessageLabel.setText("Please enter both email and password");
        }
    }

    /**
     * method to close the application
     */
    public void closeButtonAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

//    public void validateLogin(){
//        DatabaseConnection connectNow = new DatabaseConnection();
//        Connection connectDB = connectNow.getConnection();
//        String verifyLogin = "SELECT count(1) FROM user_account WHERE email = '" + emailTextField.getText() + "' AND password = '"+ passwordField.getText() + "'";
//
//        try{
//            Statement statement = connectDB.createStatement();
//            ResultSet queryResult = statement.executeQuery(verifyLogin);
//
//            while(queryResult.next()){
//                if(queryResult.getInt(1) == 1) {
//                    loginMessageLabel.setText("Correct Login");
//                }
//                else{
//                    loginMessageLabel.setText("Invalid Login. Try again or sign up");
//                }
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            e.getCause();
//        }
//    }

    /**
     * closes the current stage and loads a new stage with the registration content
     */
    public void createAccountForm(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
            Stage registerStage = new Stage();
            Stage currentStage = (Stage) signButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setTitle("Create Account");
            registerStage.setScene(scene);
            registerStage.show();
            currentStage.hide();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public void loadHomePage(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
            Stage homepageStage = new Stage();
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            homepageStage.initStyle(StageStyle.UNDECORATED);
            homepageStage.setTitle("HomePage");
            homepageStage.setScene(scene);
            homepageStage.show();
            currentStage.hide();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public void onHomePracButton(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameplay.fxml"));
            Stage practiceStage = new Stage();
            Stage currentStage = (Stage) homepagePracButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            practiceStage.initStyle(StageStyle.UNDECORATED);
            practiceStage.setTitle("Practice");
            practiceStage.setScene(scene);
            practiceStage.show();
            currentStage.hide();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public void onLogout(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
            Stage logoutStage = new Stage();
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            logoutStage.initStyle(StageStyle.UNDECORATED);
            logoutStage.setTitle("Login");
            logoutStage.setScene(scene);
            logoutStage.show();
            currentStage.hide();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }
}