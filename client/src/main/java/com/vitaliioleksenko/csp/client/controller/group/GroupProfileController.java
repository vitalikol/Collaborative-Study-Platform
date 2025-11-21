package com.vitaliioleksenko.csp.client.controller.group;

import com.vitaliioleksenko.csp.client.model.group.GroupDetailed;
import com.vitaliioleksenko.csp.client.model.membership.MembershipShort;
import com.vitaliioleksenko.csp.client.model.user.UserPartial;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.GroupService;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import javafx.collections.FXCollections;
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
    @FXML private ListView<MembershipShort> membersListView;

    private final GroupService groupService;
    private final UserSession session;
    private final DateTimeFormatter formatter;
    private final boolean isAdmin;

    public GroupProfileController() {
        this.groupService  = new GroupService();
        this.session = UserSession.getInstance();
        this.formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        this.isAdmin = session.getCurrentUserRole() == Role.ROLE_ADMIN;
    }

    public void initData(int groupId) {
        membersListView.setCellFactory(lv -> new ListCell<MembershipShort>() {
            @Override
            protected void updateItem(MembershipShort membership, boolean empty) {
                super.updateItem(membership, empty);
                setText(empty || membership == null ? "" : membership.getUser().getEmail() + " (" + membership.getRole() + ")");
            }
        });


        if (isAdmin) {
            adminActionsBox.setVisible(true);
            adminActionsBox.setManaged(true);
        }

        loadGroupDetails(groupId);
    }

    private void loadGroupDetails(int groupId) {
        try {
            GroupDetailed details = groupService.getGroupById(groupId);

            groupNameLabel.setText(details.getName());
            descriptionArea.setText(details.getDescription());
            createdByLabel.setText("Created: " + details.getCreatedBy().getEmail());
            createdAtLabel.setText("Date: " + details.getCreatedAt().format(formatter));
            membersListView.setItems(FXCollections.observableArrayList(details.getMembers()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
