package com.example.FACT.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable {
    @FXML
    private ImageView brandingImageView;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingfile = new File("images/logo.png");
        Image brandingImage = new Image(brandingfile.toURI().toString());
        brandingImageView.setImage(brandingImage);

    }
    public void closeButtonAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeButtonAction(){
        //somehow minimize -> don't think will work currently b/c our fxml is a set size
    }
}
