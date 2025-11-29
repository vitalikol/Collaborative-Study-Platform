package com.vitaliioleksenko.csp.client.controller.group;

import com.vitaliioleksenko.csp.client.model.group.GroupCreate;
import com.vitaliioleksenko.csp.client.service.GroupService;
import com.vitaliioleksenko.csp.client.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

public class GroupCreateController {

    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;

    private final GroupService groupService;
    private final UserSession session;

    @Setter private Consumer<Void> closeCallback;

    public GroupCreateController() {
        this.groupService = new GroupService();
        this.session = UserSession.getInstance();
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

        GroupCreate dto = new GroupCreate();
        dto.setName(nameField.getText().trim());
        dto.setDescription(descriptionArea.getText().trim());

        dto.setCreatedBy(session.getCurrentUserId());

        try {
            groupService.createGroup(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success", "The group has been successfully created!");

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