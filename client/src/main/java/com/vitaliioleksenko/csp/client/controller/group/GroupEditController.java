package com.vitaliioleksenko.csp.client.controller.group;

import com.vitaliioleksenko.csp.client.model.group.GroupDetailed;
import com.vitaliioleksenko.csp.client.model.group.GroupUpdate;
import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.model.user.UserUpdate;
import com.vitaliioleksenko.csp.client.service.GroupService;
import com.vitaliioleksenko.csp.client.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

public class GroupEditController {
    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;

    private int groupId;
    private final GroupService groupService;

    @Setter
    private Consumer<Void> closeCallback;

    public GroupEditController() {
        this.groupService = new GroupService();
    }

    public void initData(int id){
        groupId = id;
        try {
            GroupDetailed group = groupService.getGroupById(id);
            nameField.setText(group.getName());
            descriptionArea.setText(group.getDescription());
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML private void handleEdit() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "The user name cannot be empty.");
            return;
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "The user description cannot be empty.");
            return;
        }

        GroupUpdate groupUpdate = GroupUpdate.builder()
                .name(nameField.getText().trim())
                .description(descriptionArea.getText().trim())
                .build();

        try {
            groupService.editGroup(groupUpdate, groupId);
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
