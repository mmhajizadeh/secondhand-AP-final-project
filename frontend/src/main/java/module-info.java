module com.secondhand.frontend {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.secondhand.frontend to javafx.fxml;
    exports com.secondhand.frontend;
}