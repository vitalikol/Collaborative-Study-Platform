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


    opens com.vitaliioleksenko.csp.client.model.user to tools.jackson.databind;
    opens com.vitaliioleksenko.csp.client.model.task to tools.jackson.databind;
    opens com.vitaliioleksenko.csp.client.model.resource to tools.jackson.databind;
    opens com.vitaliioleksenko.csp.client.model.membership to tools.jackson.databind;
    opens com.vitaliioleksenko.csp.client.model.group to tools.jackson.databind;
    opens com.vitaliioleksenko.csp.client.model.activity to tools.jackson.databind;
    opens com.vitaliioleksenko.csp.client.model to tools.jackson.databind;
    opens com.vitaliioleksenko.csp.client to javafx.fxml;
    opens com.vitaliioleksenko.csp.client.controller to javafx.fxml;

    exports com.vitaliioleksenko.csp.client;
    opens com.vitaliioleksenko.csp.client.util to tools.jackson.databind;
    opens com.vitaliioleksenko.csp.client.controller.group to javafx.fxml;
    opens com.vitaliioleksenko.csp.client.controller.auth to javafx.fxml;
    opens com.vitaliioleksenko.csp.client.controller.task to javafx.fxml;
    opens com.vitaliioleksenko.csp.client.controller.calendar to javafx.fxml;
    opens com.vitaliioleksenko.csp.client.controller.settings to javafx.fxml;
    opens com.vitaliioleksenko.csp.client.controller.user to javafx.fxml;
    opens com.vitaliioleksenko.csp.client.controller.logs to javafx.fxml;
}