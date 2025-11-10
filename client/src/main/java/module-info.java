module com.vitaliioleksenko.csp.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.vitaliioleksenko.csp.client to javafx.fxml;
    exports com.vitaliioleksenko.csp.client;
}