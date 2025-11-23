package com.vitaliioleksenko.csp.client.controller.group;

import com.vitaliioleksenko.csp.client.controller.DashboardController;
import com.vitaliioleksenko.csp.client.controller.user.UserViewController;
import com.vitaliioleksenko.csp.client.model.group.GroupDetailed;
import com.vitaliioleksenko.csp.client.model.membership.MembershipCreate;
import com.vitaliioleksenko.csp.client.model.membership.MembershipShort;
import com.vitaliioleksenko.csp.client.model.user.UserPartial;
import com.vitaliioleksenko.csp.client.service.MembershipService;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.GroupService;
import com.vitaliioleksenko.csp.client.util.enums.GroupRole;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Consumer;

public class GroupProfileController {
    @FXML private Label groupNameLabel;
    @FXML private HBox adminActionsBox;
    @FXML private TextArea descriptionArea;
    @FXML private Label createdByLabel;
    @FXML private Label createdAtLabel;
    @FXML private Button addParticipantButton;
    @FXML private Button editGroupButton;
    @FXML private ListView<MembershipShort> membersListView;
    @Setter private Consumer<Void> groupEditCallback;
    @Setter private Runnable backNavigationCallback;
    @Setter private BorderPane mainBorderPane;

    private final GroupService groupService;
    private final MembershipService membershipService;
    private final UserSession session;
    private final DateTimeFormatter formatter;
    private final boolean isAdmin;
    private int groupId;

    public GroupProfileController() {
        this.groupService  = new GroupService();
        this.membershipService = new MembershipService();
        this.session = UserSession.getInstance();
        this.formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        this.isAdmin = session.getCurrentUserRole() == Role.ROLE_ADMIN;
    }

    public void initData(int groupId) {
        this.groupId = groupId;
        membersListView.setCellFactory(lv -> new ListCell<MembershipShort>() {
            @Override
            protected void updateItem(MembershipShort membership, boolean empty) {
                super.updateItem(membership, empty);
                setText(empty || membership == null ? "" : membership.getUser().getEmail() + " (" + membership.getRole() + ")");
            }
        });

        loadGroupDetails();
    }

    @FXML private void onAddParticipant() {
        DashboardController dashboard = (DashboardController)
                mainBorderPane.getScene().getRoot().getUserData();

        dashboard.showUserSelectionForGroup(groupId);
    }

    @FXML private void onEditGroup() {
        if (groupEditCallback != null) {
            groupEditCallback.accept(null);
        }
    }

    @FXML private void onDeleteGroup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("If you delete a group, all resources and tasks in that group will be deleted.");

        Stage stage = (Stage) groupNameLabel.getScene().getWindow();
        alert.initOwner(stage);

        ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(deleteButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == deleteButton) {
            try {
                groupService.deleteGroup(groupId);
                if (backNavigationCallback != null) {
                    backNavigationCallback.run();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadGroupDetails() {
        try {
            GroupDetailed details = groupService.getGroupById(groupId);

            groupNameLabel.setText(details.getName());
            descriptionArea.setText(details.getDescription());
            createdByLabel.setText("Created: " + details.getCreatedBy().getEmail());
            createdAtLabel.setText("Date: " + details.getCreatedAt().format(formatter));
            membersListView.setItems(FXCollections.observableArrayList(details.getMembers()));

            //todo add role check
            boolean isCreator = details.getCreatedBy().getUserId() == session.getCurrentUser().getUserId();

            if (isAdmin || isCreator) {
                adminActionsBox.setVisible(true);
                adminActionsBox.setManaged(true);
            } else {
                adminActionsBox.setVisible(false);
                adminActionsBox.setManaged(false);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
