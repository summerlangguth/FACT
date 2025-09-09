package com.example.FACT.controller;
import com.example.FACT.HelloApplication;
import com.example.FACT.model.ICreateSetDAO;
import com.example.FACT.model.KeySets;
import com.example.FACT.model.SqliteCreateSetDAO;
import com.example.FACT.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
    private TextField KeyBindTextField;


    public void CreateKeySet(){
        String application = ApplicationTextField.getText();
        String category = CategoryTextField.getText();
        String difficulty = DifficultyTextField.getText();
        String description = DescriptionTextField.getText();
        String keyBind = KeyBindTextField.getText();
        KeySets keyset = new KeySets(application, category, difficulty, description, keyBind);
    }
}
