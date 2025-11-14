package com.vitaliioleksenko.csp.client.controller;

import com.vitaliioleksenko.csp.client.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskCardController {

    @FXML private Label taskTitleLabel;
    @FXML private Label groupNameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label dateLabel;
    @FXML private Label statusLabel;

    // Форматер для LocalDateTime (змінено з LocalDate)
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    /**
     * Заповнює FXML-картку даними з об'єкта Task
     */
    public void setData(Task task) {
        taskTitleLabel.setText(task.getTitle());
        groupNameLabel.setText("task.getGroupName()");
        descriptionLabel.setText(task.getDescription());
        statusLabel.setText(task.getStatus().toUpperCase()); // Наприклад, "NEW", "IN_PROGRESS"
        dateLabel.setText(task.getDeadline().format(formatter));

        // Додамо трохи стилізації для статусу
        updateStatusStyle(task.getStatus());
    }

    /**
     * (Необов'язково) Допоміжний метод для кольорового статусу
     */
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