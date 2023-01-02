module c195.EMS {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens model to javafx.fxml;
    exports model;

    exports main;
    opens main to javafx.fxml;
    exports controller;
    opens controller to javafx.fxml;
}