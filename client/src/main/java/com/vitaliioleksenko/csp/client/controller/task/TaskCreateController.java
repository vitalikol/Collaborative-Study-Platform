package com.vitaliioleksenko.csp.client.controller.task;

import com.vitaliioleksenko.csp.client.model.group.GroupPartial;
import com.vitaliioleksenko.csp.client.model.task.TaskCreate;
import com.vitaliioleksenko.csp.client.service.TaskService;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.util.enums.TaskStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Consumer;

public class TaskCreateController {
    @FXML private Label headerLabel;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker deadlinePicker;

    private GroupPartial preSelectedGroup;
    private final TaskService taskService;
    private final UserSession session;

    @Setter private Consumer<Void> closeCallback;

    public TaskCreateController() {
        this.taskService = new TaskService();
        this.session = UserSession.getInstance();
    }

    public void setPreSelectedGroup(GroupPartial group) {
        this.preSelectedGroup = group;
        headerLabel.setText("Creating a new task in " + group.getName());
    }

    @FXML private void handleCreate() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "The group name cannot be empty.");
            return;
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "The group description cannot be empty.");
            return;
        }

        if (deadlinePicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Error", "The deadline description cannot be empty.");
            return;
        }

        LocalDateTime deadlineDateTime = deadlinePicker.getValue().atTime(LocalTime.MAX);

        TaskCreate taskCreateDTO = TaskCreate.builder()
                .title(nameField.getText().trim())
                .description(descriptionArea.getText().trim())
                .groupId(preSelectedGroup.getGroupId())
                .userId(session.getCurrentUserId())
                .status(TaskStatus.IN_PROGRESS)
                .deadline(deadlineDateTime)
                .build();

        try {
            taskService.createTask(taskCreateDTO);
            showAlert(Alert.AlertType.INFORMATION, "Success", "The task has been successfully created!");

            if (closeCallback != null) {
                closeCallback.accept(null);
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create group: " + e.getMessage());
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
