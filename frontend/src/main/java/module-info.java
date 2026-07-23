module com.secondhand.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.rmi;
    requires java.desktop;

    opens com.secondhand.frontend to javafx.fxml;
    opens com.secondhand.frontend.controller to javafx.fxml;
    opens com.secondhand.frontend.service.dto to com.fasterxml.jackson.databind;
    opens com.secondhand.frontend.model to com.fasterxml.jackson.databind, javafx.fxml;

    exports com.secondhand.frontend;
    exports com.secondhand.frontend.controller;
    exports com.secondhand.frontend.service;
    exports com.secondhand.frontend.service.dto;
    exports com.secondhand.frontend.session;
    exports com.secondhand.frontend.model;
    exports com.secondhand.frontend.util;
    opens com.secondhand.frontend.service to com.fasterxml.jackson.databind, javafx.fxml;
}