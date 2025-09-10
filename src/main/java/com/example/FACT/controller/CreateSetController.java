package com.example.FACT.controller;
import com.example.FACT.HelloApplication;
import com.example.FACT.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
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


public class CreateSetController {
    @FXML
    private TextField ApplicationTextField;
    @FXML
    private TextField CategoryTextField;
    @FXML
    private TextField DifficultyTextField;
    @FXML
    private TextField DescriptionTextField;

    @FXML
    private Button CancelButton;
    @FXML
    private Button SaveButton;
    @FXML
    private Button AddAnotherButton;




    @FXML
    private TextField KeyBindTextField;
    public ICreateSetDAO model = new SqliteCreateSetDAO();


    public void CreateKeySet(){
        String application = ApplicationTextField.getText();
        String category = CategoryTextField.getText();
        String difficulty = DifficultyTextField.getText();
        String description = DescriptionTextField.getText();
        String keyBind = KeyBindTextField.getText();
        KeySets keySets = new KeySets(application, category, difficulty, description, keyBind);

        //try{
           // if (model.addKeySet(keySets)) {
             //   NewKeySetMessageLabel.setText("Created New Keyset");
            //}
            //else{
              //  NewKeySetMessageLabel.setText("Please ensure all details are valid");
            //}
        //} catch (Exception e) {
          //  e.printStackTrace();
            //e.getCause();
        //}
    }


}
