package com.vitaliioleksenko.csp.client.controller.group;

import com.vitaliioleksenko.csp.client.controller.DashboardController;
import com.vitaliioleksenko.csp.client.controller.user.UserViewController;
import com.vitaliioleksenko.csp.client.model.group.GroupDetailed;
import com.vitaliioleksenko.csp.client.model.membership.MembershipCreate;
import com.vitaliioleksenko.csp.client.model.membership.MembershipShort;
import com.vitaliioleksenko.csp.client.model.membership.MembershipUpdate;
import com.vitaliioleksenko.csp.client.model.user.UserPartial;
import com.vitaliioleksenko.csp.client.service.MembershipService;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.GroupService;
import com.vitaliioleksenko.csp.client.util.enums.GroupRole;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    @Setter private Consumer<Void> addParticipantCallback;
    @Setter private Runnable backNavigationCallback;


    private final GroupService groupService;
    private final MembershipService membershipService;
    private final UserSession session;
    private final DateTimeFormatter formatter;
    private GroupDetailed details;
    private int groupId;

    public GroupProfileController() {
        this.groupService = new GroupService();
        this.membershipService = new MembershipService();
        this.session = UserSession.getInstance();
        this.formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
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
        if (addParticipantCallback != null) {
            addParticipantCallback.accept(null);
        }
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
            details = groupService.getGroupById(groupId);

            boolean isAdmin = session.getCurrentUserRole() == Role.ROLE_ADMIN;
            boolean isCreator = session.getCurrentUserId() == details.getCreatedBy().getUserId();
            boolean isTeamLead = checkGroupRole(GroupRole.TEAM_LEAD);

            groupNameLabel.setText(details.getName());
            descriptionArea.setText(details.getDescription());
            createdByLabel.setText("Created: " + details.getCreatedBy().getEmail());
            createdAtLabel.setText("Date: " + details.getCreatedAt().format(formatter));
            membersListView.setItems(FXCollections.observableArrayList(details.getMembers()));

            if (isAdmin || isCreator || isTeamLead) {
                setupListViewFactory();
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

    private boolean checkGroupRole(GroupRole role){
        List<MembershipShort> members = details.getMembers();

        for (MembershipShort member: members){
            if ((member.getUser().getUserId() == session.getCurrentUserId()) && (member.getRole() == role)){
                return true;
            }
        }

        return false;
    }

    public void handleRemoval(MembershipShort membership) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove member");
        alert.setHeaderText("Remove " + membership.getUser().getEmail() + " from this group?");

        Stage stage = (Stage) groupNameLabel.getScene().getWindow();
        alert.initOwner(stage);

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                membershipService.deleteMembership(membership.getMembershipId());

                loadGroupDetails();
            } catch (Exception e) {
                throw new RuntimeException(e);
                //showError(e.getMessage());
            }
        }
    }

    private void setupListViewFactory() {
        membersListView.setCellFactory(lv -> new GroupListCell(this));
    }

    static class GroupListCell extends ListCell<MembershipShort> {

        private final HBox container = new HBox(10);

        private final Label label = new Label();

        private final Button roleBtn = new Button("Change role");
        private final Button removeBtn = new Button("Remove");

        // Елементи для редагування ролі
        private final ComboBox<GroupRole> roleCombo = new ComboBox<>();
        private final Button confirmBtn = new Button("Confirm");
        private final Button cancelBtn = new Button("Cancel");

        private final Region spacer = new Region();

        private boolean editingRole = false;

        private final GroupProfileController controller;

        public GroupListCell(GroupProfileController controller) {
            this.controller = controller;

            container.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(spacer, Priority.ALWAYS);

            roleBtn.setOnAction(e -> startEditRole());

            removeBtn.setOnAction(e -> {
                MembershipShort m = getItem();
                if (m != null) controller.handleRemoval(m);
            });

            confirmBtn.setOnAction(e -> applyRoleChange());
            cancelBtn.setOnAction(e -> stopEditRole());

            roleCombo.getItems().setAll(GroupRole.values());
        }

        @Override
        protected void updateItem(MembershipShort membership, boolean empty) {
            super.updateItem(membership, empty);

            if (empty || membership == null) {
                setGraphic(null);
                return;
            }

            label.setText(membership.getUser().getEmail() + " (" + membership.getRole() + ")");

            if (editingRole) {
                roleCombo.setValue(membership.getRole());

                container.getChildren().setAll(
                        label,
                        spacer,
                        roleCombo,
                        confirmBtn,
                        cancelBtn
                );
            } else {
                // Стандартний стан
                container.getChildren().setAll(
                        label,
                        spacer,
                        roleBtn,
                        removeBtn
                );
            }

            setGraphic(container);
        }

        private void startEditRole() {
            editingRole = true;
            updateItem(getItem(), false); // пере-рендер
        }

        private void stopEditRole() {
            editingRole = false;
            updateItem(getItem(), false); // пере-рендер
        }

        private void applyRoleChange() {
            MembershipShort m = getItem();
            if (m == null) return;

            GroupRole newRole = roleCombo.getValue();
            if (newRole == null || newRole == m.getRole()) {
                stopEditRole();
                return;
            }
            try {
                controller.membershipService.editMembership(new MembershipUpdate(newRole), getItem().getMembershipId());

                m.setRole(newRole);
                stopEditRole();
                controller.loadGroupDetails();
            } catch (Exception e) {
                throw new RuntimeException(e);
                //controller.showError(e.getMessage());
            }
        }
    }
}
