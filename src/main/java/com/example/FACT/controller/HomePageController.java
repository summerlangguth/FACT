package com.example.FACT.controller;

import com.example.FACT.HelloApplication;
import com.example.FACT.model.GameEngine;
import com.example.FACT.model.Shortcut;
import com.example.FACT.model.SqliteUserDAO;
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
    private Button logoutButton;



    @FXML
    private void initialize(){
        String userName = UserManager.getInstance().getLoggedInUser().getFirstName();
        Integer active = UserManager.getInstance().getLoggedInUser().getActivity();
        welcomeMessage.setText("Welcome, " + userName);
        activityStreak.setText("Current Streak: " + active);
    }

    public void onLogout(){
        try{
            FXMLLoader baseLoader = new FXMLLoader(HelloApplication.class.getResource("authbase.fxml"));
            Parent root = baseLoader.load();
            Stage logoutStage = new Stage();
            AuthBaseController baseController = baseLoader.getController();
            baseController.setContent("/com/example/FACT/login.fxml");
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root);
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
