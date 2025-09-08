module com.example.FACT {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;


    opens com.example.FACT to javafx.fxml;
    exports com.example.FACT;
    exports com.example.FACT.controller;
    opens com.example.FACT.controller to javafx.fxml;
    exports com.example.FACT.model;
    opens com.example.FACT.model to javafx.fxml;
}