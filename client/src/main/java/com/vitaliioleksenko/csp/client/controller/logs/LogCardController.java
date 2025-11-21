package com.vitaliioleksenko.csp.client.controller.logs;

import com.vitaliioleksenko.csp.client.model.activity.ActivityLogDetailed;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;

public class LogCardController {
    @FXML private Label logNameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label userLabel;
    @FXML private Label dateLabel;

    public void setData(ActivityLogDetailed log) {
        logNameLabel.setText(log.getAction());
        descriptionLabel.setText(log.getDetails());
        userLabel.setText("User: " + log.getUser().getEmail());
        dateLabel.setText("Date: " + log.getTimestamp().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm")));
    }
}
