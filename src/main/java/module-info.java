module com.example.fact {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fact to javafx.fxml;
    exports com.example.fact;
}