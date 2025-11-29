package com.vitaliioleksenko.csp.client.controller.task;

import com.vitaliioleksenko.csp.client.model.task.TaskDetailed;
import com.vitaliioleksenko.csp.client.model.task.TaskPartial;
import com.vitaliioleksenko.csp.client.model.task.TaskUpdate;
import com.vitaliioleksenko.csp.client.service.TaskService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;

public class TaskEditController {
    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker deadlinePicker;

    private int taskId;
    private final TaskService taskService;

    @Setter private Consumer<TaskPartial> closeCallback;

    public TaskEditController() {
        this.taskService = new TaskService();
    }

    public void initData(int id){
        taskId = id;
        try {
            TaskDetailed task = taskService.getTaskById(id);
            nameField.setText(task.getTitle());
            descriptionArea.setText(task.getDescription());
            deadlinePicker.setValue(LocalDate.from(task.getDeadline()));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML private void handleEdit() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "The task title cannot be empty.");
            return;
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "The task description cannot be empty.");
            return;
        }

        if (deadlinePicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Error", "The task deadline cannot be empty.");
            return;
        }

        TaskUpdate taskUpdate = TaskUpdate.builder()
                .title(nameField.getText().trim())
                .description(descriptionArea.getText().trim())
                .deadline(deadlinePicker.getValue().atTime(LocalTime.MAX))
                .build();

        try {
            taskService.editTask(taskUpdate, taskId);
            showAlert(Alert.AlertType.INFORMATION, "Success", "The task has been successfully edited!");

            TaskDetailed detailed = taskService.getTaskById(taskId);


            if (closeCallback != null) {
                closeCallback.accept(TaskPartial.builder()
                                .taskId(detailed.getTaskId())
                                .group(detailed.getGroup())
                                .title(detailed.getTitle())
                                .status(detailed.getStatus())
                                .deadline(detailed.getDeadline())
                                .build());
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to edit user: " + e.getMessage());
        }
    }

    @FXML private void handleCancel() {
        if (closeCallback != null) {
            closeCallback.accept(null);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Stage stage = (Stage) nameField.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }
}
