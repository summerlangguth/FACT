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
    private Button homepagePracButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button createButton;


    public void onHomePracButton(){
        try{

            // Load homebase layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/FACT/gameplay.fxml"));
            Parent root = loader.load();

            // Get controller of homebase
            GameplayController controller = loader.getController();

            List<Shortcut> demoShortcuts = List.of(
                    new Shortcut("Copy", new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN)),
                    new Shortcut("Paste", new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN))
            );

            // Load homepage.fxml into the content area
            GameEngine engine = new GameEngine(demoShortcuts);
            controller.setEngine(engine);

            // Get current stage from the login button
            Stage newStage = new Stage();
            Stage currentStage = (Stage) homepagePracButton.getScene().getWindow();

            // Set the new scene with the base layout
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
            currentStage.hide();


            /*
            // Load homebase layout
            FXMLLoader baseLoader = new FXMLLoader(HelloApplication.class.getResource("homebase.fxml"));
            Parent root = baseLoader.load();

            // Get controller of homebase
            HomeBaseController baseController = baseLoader.getController();

            // Load homepage.fxml into the content area
            baseController.setContent("/com/example/FACT/gameplay.fxml");

            // Get current stage from the login button
            Stage newStage = new Stage();
            Stage currentStage = (Stage) homepagePracButton.getScene().getWindow();
            // Set the new scene with the base layout
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
            currentStage.hide();
            */
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
            // Load homebase layout
            FXMLLoader baseLoader = new FXMLLoader(HelloApplication.class.getResource("homebase.fxml"));
            Parent root = baseLoader.load();

            // Get controller of homebase
            HomeBaseController baseController = baseLoader.getController();

            // Load homepage.fxml into the content area
            baseController.setContent("/com/example/FACT/createSet.fxml");

            // Get current stage from the login button
            Stage newStage = new Stage();
            Stage currentStage = (Stage) createButton.getScene().getWindow();
            // Set the new scene with the base layout
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
            currentStage.hide();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

}
