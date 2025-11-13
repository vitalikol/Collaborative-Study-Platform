package com.vitaliioleksenko.csp.client.controller;

import com.vitaliioleksenko.csp.client.model.LoginRequest;
import com.vitaliioleksenko.csp.client.model.RegisterRequest;
import com.vitaliioleksenko.csp.client.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField rePasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    private final AuthService authService;

    public RegisterController() {
        this.authService = new AuthService();
    }

    @FXML
    public void initialize(){
        errorLabel.setVisible(false);
    }

    @FXML
    public void handleRegister(){
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
        } else {
            showError("Wrong email or password");
        }
    }

    @FXML
    public void handleLogin(){
        openLoginWindow();
    }

    private void openLoginWindow(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage courrentStage = (Stage)loginButton.getScene().getWindow();

            Stage mainStage = new Stage();
            mainStage.setTitle("Login");
            mainStage.setScene(scene);

            courrentStage.close();
            mainStage.show();
        } catch (IOException _){}
            showError("Can't open login window");
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
