package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.SqliteUserDAO;
import com.example.FACT.model.User;
import com.example.FACT.model.UserManager;
import javafx.application.Platform;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;

import java.net.URL;
import java.util.Objects;

import java.io.File;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private BorderPane root;
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
        Platform.runLater(() -> {
                root.getScene().addEventFilter((KeyEvent.KEY_PRESSED), this::onKeyPressed);
        });

    }

    private void onKeyPressed(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER){
            loginButtonOnAction();
        }
    }

    /**
     * creates the registration form
     */
    public void signButtonOnAction(){
        createAccountForm();
    }

    /**
     * validates user credentials and calls isLogin to check.
     */
    public void loginButtonOnAction(){
        loginMessageLabel.setText("");
        if(!emailTextField.getText().isBlank() && !passwordField.getText().isBlank()){
            try{
                String email = emailTextField.getText();
                String password = passwordField.getText();
                if(model.isLogin(email, password)){
                    //loginMessageLabel.setText("valid login");
                    User loggedInUser = model.createUserObject(email, password);
                    UserManager.getInstance().setLoggedInUser(loggedInUser);
                    model.setActivity(email);
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

    public void createAccountForm(){
        try{
            FXMLLoader baseLoader = new FXMLLoader(HelloApplication.class.getResource("authbase.fxml"));
            Parent root = baseLoader.load();
            Stage logoutStage = new Stage();
            AuthBaseController baseController = baseLoader.getController();
            baseController.setContent("/com/example/FACT/register.fxml");
            Stage currentStage = (Stage) signButton.getScene().getWindow();
            Scene scene = new Scene(root);
            logoutStage.initStyle(StageStyle.UNDECORATED);
            logoutStage.setTitle("Register");
            logoutStage.setScene(scene);
            logoutStage.show();
            currentStage.hide();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    public void loadHomePage() {
        try {
            // Load base shell
            URL baseUrl = getClass().getResource("/com/example/FACT/homebase.fxml");
            FXMLLoader baseLoader = new FXMLLoader(Objects.requireNonNull(baseUrl, "homebase.fxml not found"));
            Parent baseRoot = baseLoader.load();

            // Ask BaseController to show Home inside the center
            com.example.FACT.controller.BaseController baseController = baseLoader.getController();
            baseController.setContent("/com/example/FACT/homepage.fxml");

            // Reuse the current stage
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(baseRoot));
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}