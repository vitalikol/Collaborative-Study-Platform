package com.vitaliioleksenko.csp.client.controller.user;

import com.vitaliioleksenko.csp.client.model.group.GroupPartial;
import com.vitaliioleksenko.csp.client.model.task.TaskCreate;
import com.vitaliioleksenko.csp.client.model.user.UserUpdate;
import com.vitaliioleksenko.csp.client.service.TaskService;
import com.vitaliioleksenko.csp.client.service.UserService;
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

public class UserEditController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;

    private final UserService userService;
    private final UserSession session;

    @Setter
    private Consumer<Void> closeCallback;

    public UserEditController() {
        this.userService = new UserService();
        this.session = UserSession.getInstance();
    }

    @FXML private void handleEdit() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "The user name cannot be empty.");
            return;
        }

        if (emailField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "The user description cannot be empty.");
            return;
        }

        UserUpdate userUpdate = UserUpdate.builder()
                .name(nameField.getText().trim())
                .email(emailField.getText().trim())
                .build();

        try {
            userService.editUser(userUpdate, session.getCurrentUserId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "The user has been successfully edited!");

            if (closeCallback != null) {
                closeCallback.accept(null);
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
