package com.example.FACT.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeBaseController implements Initializable {

    @FXML
    private StackPane contentArea;
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
    // Method to load different FXML into the content area
    public void setContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node node = loader.load();
            contentArea.getChildren().setAll(node); // replace existing content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
