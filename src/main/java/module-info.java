module com.example.demo18 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo18 to javafx.fxml;
    exports com.example.demo18;
}