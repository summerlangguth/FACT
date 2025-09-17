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

public class HomePageController {
    @FXML
    private Button homepagePracButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button createButton;

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

    public void onCreateButton(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createset.fxml"));
            Stage createStage = new Stage();
            Stage currentStage = (Stage) createButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            createStage.initStyle(StageStyle.UNDECORATED);
            createStage.setTitle("Create");
            createStage.setScene(scene);
            createStage.show();
            currentStage.hide();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

}
