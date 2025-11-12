module com.vitaliioleksenko.csp.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires annotations;
    requires okhttp3.logging;
    requires okhttp3;
    requires java.net.http;

    opens com.vitaliioleksenko.csp.client.model to com.fasterxml.jackson.databind;
    opens com.vitaliioleksenko.csp.client to javafx.fxml;
    opens com.vitaliioleksenko.csp.client.controller to javafx.fxml;

    exports com.vitaliioleksenko.csp.client;
}