module com.vitaliioleksenko.csp.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires tools.jackson.databind;
    requires okhttp3;
    requires okio;
    requires okhttp3.logging;
    requires kotlin.stdlib;
    requires lombok;
    requires annotations;


    opens com.vitaliioleksenko.csp.client.model to tools.jackson.databind;
    opens com.vitaliioleksenko.csp.client to javafx.fxml;
    opens com.vitaliioleksenko.csp.client.controller to javafx.fxml;

    exports com.vitaliioleksenko.csp.client;
    opens com.vitaliioleksenko.csp.client.util to tools.jackson.databind;
}