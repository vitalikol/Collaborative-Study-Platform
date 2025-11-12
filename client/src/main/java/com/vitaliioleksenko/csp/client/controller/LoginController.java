package com.vitaliioleksenko.csp.client.controller;

import com.vitaliioleksenko.csp.client.model.LoginRequest;
import com.vitaliioleksenko.csp.client.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorLabel;

    private final AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Будь ласка, заповніть всі поля");
            return;
        }

        if (authenticate(username, password)) {
            errorLabel.setVisible(false);
            showSuccess("Вхід успішний!");
            openMainWindow();
        } else {
            showError("Невірний логін або пароль");
        }
    }

    @FXML
    private void handleCancel() {
        usernameField.clear();
        passwordField.clear();
        errorLabel.setVisible(false);
    }

    private boolean authenticate(String username, String password)  {
        LoginRequest credentials = new LoginRequest(username, password);

        try {
            authService.login(credentials);
            return true;
        } catch (IOException e){
            return false;
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успіх");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/hello.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 600);

            Stage loginStage = (Stage) loginButton.getScene().getWindow();

            Stage mainStage = new Stage();
            mainStage.setTitle("Hello");
            mainStage.setScene(scene);
            mainStage.setMinWidth(800);
            mainStage.setMinHeight(600);

            loginStage.close();

            mainStage.show();

        } catch (IOException e) {
            showError("Помилка при завантаженні головного екрану");
        }
    }
}
