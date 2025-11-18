package com.vitaliioleksenko.csp.client.controller.user;

import com.vitaliioleksenko.csp.client.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class UserCardController {

    @FXML private ImageView avatarView;
    @FXML private Label fullNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;

    public void setData(User user) {
        fullNameLabel.setText(user.getName());
        emailLabel.setText(user.getEmail());
        roleLabel.setText(user.getRole().toUpperCase());
    }
}
