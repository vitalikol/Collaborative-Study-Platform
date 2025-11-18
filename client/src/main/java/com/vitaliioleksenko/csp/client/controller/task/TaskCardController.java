package com.vitaliioleksenko.csp.client.controller.task;

import com.vitaliioleksenko.csp.client.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;

public class TaskCardController {

    @FXML private Label taskTitleLabel;
    @FXML private Label groupNameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label dateLabel;
    @FXML private Label statusLabel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    public void setData(Task task) {
        taskTitleLabel.setText(task.getTitle());
        groupNameLabel.setText("task.getGroupName()");
        descriptionLabel.setText(task.getDescription());
        statusLabel.setText(task.getStatus().toUpperCase());
        dateLabel.setText(task.getDeadline().format(formatter));

        updateStatusStyle(task.getStatus());
    }
    private void updateStatusStyle(String status) {
        String style = "-fx-text-fill: #FFFFFF; -fx-background-radius: 4; -fx-padding: 2 5;";

        switch (status.toLowerCase()) {
            case "new":
                statusLabel.setStyle(style + " -fx-background-color: #007BFF;"); // Cиній
                break;
            case "in_progress":
                statusLabel.setStyle(style + " -fx-background-color: #FFA500;"); // Помаранчевий
                break;
            case "done":
                statusLabel.setStyle(style + " -fx-background-color: #28A745;"); // Зелений
                break;
            case "archived":
                statusLabel.setStyle(style + " -fx-background-color: #6C757D;"); // Cірий
                break;
            default:
                statusLabel.setStyle("-fx-text-fill: #333333;");
                break;
        }
    }
}