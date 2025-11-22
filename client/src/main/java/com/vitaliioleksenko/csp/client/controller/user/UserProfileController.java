package com.vitaliioleksenko.csp.client.controller.user;

import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.AuthService;
import com.vitaliioleksenko.csp.client.service.UserService;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

public class UserProfileController {
    @FXML private ImageView avatarView;
    @FXML private Label fullNameLabel;
    @FXML private Label roleLabel;
    @FXML private VBox actionButtonsBox;
    @FXML private Button editProfileButton;
    @FXML private Button changePasswordButton;
    @FXML private Button adminActionsButton;
    @FXML private Button deleteProfileButton;
    @FXML private GridPane userInfoGrid;
    @FXML private VBox userStatsBox;
    @FXML private Label tasksCompletedLabel;
    @FXML private Label tasksInProgressLabel;
    @FXML private Label tasksOverdueLabel;
    @Setter private Consumer<Void> userEditCallback;


    private final UserService userService;
    private final AuthService authService;
    private final UserSession userSession;
    private final boolean amIAdmin;


    public UserProfileController() {
        this.userService = new UserService();
        this.authService = new AuthService();
        this.userSession = UserSession.getInstance();
        this.amIAdmin = userSession.getCurrentUserRole() == Role.ROLE_ADMIN;
    }

    @FXML private void handleEditUser() {
        if (userEditCallback != null) {
            userEditCallback.accept(null);
        }
    }

    public void initData(Integer userId) {
        boolean isMyProfile = (userId == null || userId.equals(userSession.getCurrentUserId()));

        if (isMyProfile) {
            try{
                UserDetailed user = authService.me();
                populateUI(user);
                setupMyProfileView(user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try{
                UserDetailed user = userService.getUserById(userId);
                populateUI(user);
                setupGuestProfileView(amIAdmin);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void populateUI(UserDetailed user) {
        fullNameLabel.setText(user.getName());
        roleLabel.setText(user.getRole().toString());
        tasksCompletedLabel.setText("Task completed: -" ); //+ user.getStats().getCompleted());
        tasksInProgressLabel.setText("Task in progress: -"); // + user.getStats().getInProgress());
        tasksOverdueLabel.setText("Overdue tasks: -"); // + user.getStats().getOverdue());
    }

    private void setupMyProfileView(UserDetailed me) {
        addRowToGrid(userInfoGrid, "Email", me.getEmail(), 0);
        addRowToGrid(userInfoGrid, "Team","--", 1); //me.getTeamName()
        addRowToGrid(userInfoGrid, "Registration date","--", 2); //me.getCreatedAt()

        editProfileButton.setVisible(true);
        editProfileButton.setManaged(true);
        changePasswordButton.setVisible(true);
        changePasswordButton.setManaged(true);
        deleteProfileButton.setVisible(true);
        deleteProfileButton.setManaged(true);
    }

    private void setupGuestProfileView(boolean amIAdmin) {
        addRowToGrid(userInfoGrid, "Team", "--", 0); //user.getTeamName()
        addRowToGrid(userInfoGrid, "Registration date","--" , 1); //user.getCreatedAt()

        if (amIAdmin) {
            adminActionsButton.setVisible(true);
            adminActionsButton.setManaged(true);
        }
    }

    private void addRowToGrid(GridPane grid, String key, String value, int rowIndex) {
        Label keyLabel = new Label(key);
        keyLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", 14));

        grid.add(keyLabel, 0, rowIndex);
        grid.add(valueLabel, 1, rowIndex);
    }
}