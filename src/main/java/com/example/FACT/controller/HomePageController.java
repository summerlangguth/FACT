package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.*;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController {
    @FXML
    private Label welcomeMessage;
    @FXML
    private Label activityStreak;
    @FXML
    private Label lastPlayed;
    @FXML
    private Button logoutButton;
    @FXML
    private ImageView brandingImageView;
    private final RegistrationController registrationController = new RegistrationController();



    @FXML
    private void initialize(){
        File brandingfile = new File("images/logo.png");
        Image brandingImage = new Image(brandingfile.toURI().toString());
        brandingImageView.setImage(brandingImage);
        String userName = UserManager.getInstance().getLoggedInUser().getFirstName();
        Integer active = UserManager.getInstance().getLoggedInUser().getActivity();
        String set = UserManager.getInstance().getLoggedInUser().getLastPlayed();
        welcomeMessage.setText("Welcome, " + userName);
        activityStreak.setText("Daily Streak: " + active);
        if(set == null){
            lastPlayed.setText("Start Practicing Now!");
        }
        else{
            lastPlayed.setText("Last Set Played: " + set);
        }
    }

    @FXML private void onLogout() {
        UserManager.setInstance(null);
        PasswordUtils.setInstance(null);
        registrationController.loadLogin(logoutButton);}
}
