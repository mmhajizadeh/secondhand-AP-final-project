module com.secondhand.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;


    opens com.secondhand.frontend to javafx.fxml;
    exports com.secondhand.frontend;
}