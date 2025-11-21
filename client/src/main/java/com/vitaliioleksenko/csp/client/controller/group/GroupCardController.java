package com.vitaliioleksenko.csp.client.controller.group;

import com.vitaliioleksenko.csp.client.model.group.GroupDetailed;
import com.vitaliioleksenko.csp.client.model.group.GroupPartial;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GroupCardController {
    @FXML private Label groupNameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label memberCountLabel;

    public void setData(GroupPartial group) {
        groupNameLabel.setText(group.getName());
        descriptionLabel.setText(group.getDescription());
        memberCountLabel.setText("Created: " + group.getCreatedBy().getEmail());
    }
}
