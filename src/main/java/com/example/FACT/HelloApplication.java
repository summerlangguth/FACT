package com.example.FACT;

import com.example.FACT.controller.AuthBaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Load the auth base (top bar only)
            FXMLLoader baseLoader = new FXMLLoader(
                    HelloApplication.class.getResource("authbase.fxml")
            );
            Parent root = baseLoader.load();

            // Inject the login view into the content area
            AuthBaseController base = baseLoader.getController();
            base.setContent("/com/example/FACT/login.fxml");

            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // You could show an error dialog or exit if desired
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
