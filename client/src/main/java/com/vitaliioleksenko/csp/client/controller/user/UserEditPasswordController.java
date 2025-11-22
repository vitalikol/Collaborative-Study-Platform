package com.vitaliioleksenko.csp.client.controller.user;

import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.model.user.UserUpdate;
import com.vitaliioleksenko.csp.client.service.UserService;
import com.vitaliioleksenko.csp.client.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

public class UserEditPasswordController {
    @FXML private PasswordField passwordField;
    @FXML private TextField rePasswordField;

    private int userId;
    private final UserService userService;

    @Setter
    private Consumer<Void> closeCallback;

    public UserEditPasswordController() {
        this.userService = new UserService();
    }

    public void initData(int id){
        userId = id;
    }

    @FXML private void handleChange() {
        if (!passwordField.getText().equals(rePasswordField.getText())) {
            showAlert(Alert.AlertType.WARNING, "Error", "Password's doesn't match.");
            return;
        }

        UserUpdate userUpdate = UserUpdate.builder()
                .password(passwordField.getText())
                .build();

        try {
            userService.editUser(userUpdate, userId);
            //todo auto terminate all sessions
            showAlert(Alert.AlertType.INFORMATION, "Success", "The password has been successfully changed!");

            if (closeCallback != null) {
                closeCallback.accept(null);
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to change password user: " + e.getMessage());
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

        Stage stage = (Stage) passwordField.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }
}
