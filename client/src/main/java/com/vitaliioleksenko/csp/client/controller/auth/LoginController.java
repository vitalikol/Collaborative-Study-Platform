package com.vitaliioleksenko.csp.client.controller.auth;

import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.model.user.AuthenticationRequest;
import com.vitaliioleksenko.csp.client.util.UserSession;
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
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
    private final AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    @FXML private void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        if (login(username, password)) {
            errorLabel.setVisible(false);
            WindowRenderer.switchScene((Stage) loginButton.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/dashboard.fxml");
        }
    }

    @FXML
    private void handleRegister() {
        WindowRenderer.switchScene((Stage) loginButton.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/auth/register.fxml");
    }

    private boolean login(String username, String password)  {
        AuthenticationRequest credentials = new AuthenticationRequest(username, password);
        try {
            authService.login(credentials);
            UserDetailed user = authService.me();
            UserSession.getInstance().login(user);
            return true;
        } catch (IOException e){
            showError(e.getMessage());
            return false;
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
