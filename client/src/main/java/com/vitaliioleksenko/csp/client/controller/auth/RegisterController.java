package com.vitaliioleksenko.csp.client.controller.auth;

import com.vitaliioleksenko.csp.client.model.user.AuthenticationRequest;
import com.vitaliioleksenko.csp.client.model.user.RegisterRequest;
import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.service.AuthService;
import com.vitaliioleksenko.csp.client.util.OAuthCallbackServer;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.util.WindowRenderer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

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

    @FXML
    private void handleGoogleLogin() {
        OAuthCallbackServer callbackServer = new OAuthCallbackServer();

        callbackServer.start(() -> {
            String token = callbackServer.getToken();

            UserSession.getInstance().setToken(token);

            UserDetailed user = null;
            try {
                user = authService.me();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            UserSession.getInstance().setCurrentUser(user);

            Platform.runLater(() -> {
                WindowRenderer.switchScene(
                        (Stage) loginButton.getScene().getWindow(),
                        "/com/vitaliioleksenko/csp/client/view/dashboard.fxml"
                );
            });

            callbackServer.stop();
        });

        try {
            Desktop.getDesktop().browse(new URI("http://localhost:8080/oauth2/authorization/google"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        RegisterRequest registerRequest = RegisterRequest.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        if (register(registerRequest)) {
            errorLabel.setVisible(false);
            authService.login(new AuthenticationRequest(email, password));
            WindowRenderer.switchScene((Stage) loginButton.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/dashboard.fxml");
        }
    }

    @FXML public void handleLogin(){
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
            showError(e.getMessage());
            return false;
        }
    }
}
