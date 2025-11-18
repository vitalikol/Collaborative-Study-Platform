package com.vitaliioleksenko.csp.client.controller.group;

import com.vitaliioleksenko.csp.client.model.Group;
import com.vitaliioleksenko.csp.client.model.User;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.GroupService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class GroupProfileController {

    @FXML private Label groupNameLabel;
    @FXML private HBox adminActionsBox;
    @FXML private TextArea descriptionArea;
    @FXML private Label createdByLabel;
    @FXML private Label createdAtLabel;
    @FXML private ListView<User> membersListView; // Припустимо, API повертає список User

    private final GroupService groupService = new GroupService();
    private final UserSession session = UserSession.getInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");


    public void initData(int groupId) {
        membersListView.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? "" : user.getName() + " (" + user.getRole() + ")");
            }
        });


        if (session.getCurrentUserRole().equals("ROLE_ADMIN")) {
            adminActionsBox.setVisible(true);
            adminActionsBox.setManaged(true);
        }

        loadGroupDetails(groupId);
    }

    private void loadGroupDetails(int groupId) {
        try {
            Group details = groupService.getGroupById(groupId);

            groupNameLabel.setText(details.getName());
            descriptionArea.setText(details.getDescription());
            createdByLabel.setText("Created: "); //details.getCreatedBy().getFullName()
            createdAtLabel.setText("Date: "); //details.getCreatedAt().format(formatter)
            //membersListView.setItems(FXCollections.observableArrayList(details.getMembers()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
