package com.vitaliioleksenko.csp.client.controller;

import com.vitaliioleksenko.csp.client.util.LoginRequest;
import com.vitaliioleksenko.csp.client.service.AuthService;
import com.vitaliioleksenko.csp.client.util.WindowRenderer;
import javafx.fxml.FXML;
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
            WindowRenderer.switchScene((Stage) loginButton.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/dashboard.fxml");
        } else {
            showError("Wrong email or password");
        }
    }

    @FXML
    private void handleRegister() {
        WindowRenderer.switchScene((Stage) loginButton.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/register.fxml");
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
}
