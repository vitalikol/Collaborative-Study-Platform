package com.vitaliioleksenko.csp.client.controller.auth;

import com.vitaliioleksenko.csp.client.model.user.AuthenticationRequest;
import com.vitaliioleksenko.csp.client.model.user.RegisterRequest;
import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.AuthService;
import com.vitaliioleksenko.csp.client.util.WindowRenderer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TextField rePasswordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    private final AuthService authService;

    public RegisterController() {
        this.authService = new AuthService();
    }

    @FXML public void initialize(){
        errorLabel.setVisible(false);
    }

    @FXML public void handleRegister() throws IOException {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String rePassword = rePasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        if(!password.equals(rePassword)){
            showError("Passwords is not matched");
            return;
        }
        RegisterRequest registerRequest = new RegisterRequest(name, email, password);
        if (register(registerRequest)) {
            errorLabel.setVisible(false);
            authService.login(new AuthenticationRequest(email, password));
            UserDetailed user = authService.me();
            UserSession.getInstance().login(user);
            WindowRenderer.switchScene((Stage) loginButton.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/dashboard.fxml");
        } else {
            showError("Wrong email or password");
        }
    }

    @FXML
    public void handleLogin(){
        WindowRenderer.switchScene((Stage) loginButton.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/auth/login.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private boolean register(RegisterRequest registerRequest)  {
        try {
            authService.register(registerRequest);
            return true;
        } catch (IOException e){
            return false;
        }
    }
}
