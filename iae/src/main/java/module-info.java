module com.example.iae {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.iae to javafx.fxml;
    exports com.example.iae;
}