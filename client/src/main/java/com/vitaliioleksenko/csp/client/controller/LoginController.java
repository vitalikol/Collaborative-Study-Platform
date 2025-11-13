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
            showError("Please fill all fields");
            return;
        }

        if (login(username, password)) {
            errorLabel.setVisible(false);
            openMainWindow();
        } else {
            showError("Wrong email or password");
        }
    }

    @FXML
    private void handleRegister() {
        openRegisterWindow();
    }

    private boolean login(String username, String password)  {
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
            showError("Can't open main window");
        }
    }

    private void openRegisterWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/register.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage loginStage = (Stage) loginButton.getScene().getWindow();

            Stage mainStage = new Stage();
            mainStage.setTitle("Register");
            mainStage.setScene(scene);

            loginStage.close();

            mainStage.show();

        } catch (IOException e) {
            showError("Can't open register window");
        }
    }
}
