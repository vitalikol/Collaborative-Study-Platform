package com.vitaliioleksenko.csp.client.controller;

import com.vitaliioleksenko.csp.client.model.User;
import com.vitaliioleksenko.csp.client.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;


public class HelloController {
    @FXML
    public Label nameLabel;
    @FXML
    public Label emailLabel;
    @FXML
    public Label idLabel;

    private final AuthService authService;

    public HelloController() {
        this.authService = new AuthService();
    }

    @FXML
    private void initialize() throws IOException {
        User user = authService.me();
        nameLabel.setText("Name: " + user.getName());
        emailLabel.setText("Email: " + user.getEmail());
        idLabel.setText("Id: " + user.getUserId());
    }
}
