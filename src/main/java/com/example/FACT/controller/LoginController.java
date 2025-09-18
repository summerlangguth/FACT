package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.SqliteUserDAO;
import com.example.FACT.model.User;
import com.example.FACT.model.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
                String email = emailTextField.getText();
                String password = passwordField.getText();
                if(model.isLogin(email, password)){
                    //loginMessageLabel.setText("valid login");
                    User loggedInUser = model.createUserObject(email, password);
                    UserManager.getInstance().setLoggedInUser(loggedInUser);
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
            // Load homebase layout
            FXMLLoader baseLoader = new FXMLLoader(HelloApplication.class.getResource("homebase.fxml"));
            Parent root = baseLoader.load();

            // Get controller of homebase
            HomeBaseController baseController = baseLoader.getController();

            // Load homepage.fxml into the content area
            baseController.setContent("/com/example/FACT/homepage.fxml");

            // Get current stage from the login button
            Stage newStage = new Stage();
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            // Set the new scene with the base layout
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.show();
            currentStage.hide();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }
}