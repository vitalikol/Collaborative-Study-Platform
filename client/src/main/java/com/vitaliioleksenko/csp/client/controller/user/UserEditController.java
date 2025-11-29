package com.vitaliioleksenko.csp.client.controller.user;

import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.model.user.UserUpdate;
import com.vitaliioleksenko.csp.client.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

public class UserEditController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;

    private int userId;
    private final UserService userService;

    @Setter
    private Consumer<Void> closeCallback;

    public UserEditController() {
        this.userService = new UserService();
    }

    public void initData(int id){
        userId = id;
        try {
            UserDetailed user = userService.getUserById(id);
            nameField.setText(user.getName());
            emailField.setText(user.getEmail());
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML private void handleEdit() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "The user name cannot be empty.");
            return;
        }

        if (emailField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "The user email cannot be empty.");
            return;
        }

        UserUpdate userUpdate = UserUpdate.builder()
                .name(nameField.getText().trim())
                .email(emailField.getText().trim())
                .build();

        try {
            userService.editUser(userUpdate, userId);
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
