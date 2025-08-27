module com.example.FACT {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;


    opens com.example.FACT to javafx.fxml;
    exports com.example.FACT;
}