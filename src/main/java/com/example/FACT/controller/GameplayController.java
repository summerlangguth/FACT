package com.example.FACT.controller;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
//import javafx.scene.control.FlowPane;
import javafx.scene.control.Label;
//import javafx.scene.media.MediaView;
import javafx.scene.text.Text;

public class GameplayController {
    @FXML private Node root;
    @FXML private Label appTitleLabel;
    @FXML private Label shortcutTitleLabel;
    @FXML private Text  shortcutDescText;
    //@FXML private FlowPane keysPane;
    @FXML private Label keysHintLabel;
    //@FXML private MediaView mediaView;
    @FXML private Button playBtn, pauseBtn, replayBtn;
    @FXML private Label timeLabel;

    @FXML
    public void initialize() {}
}