package com.vitaliioleksenko.csp.client.controller.user;

import com.vitaliioleksenko.csp.client.model.UserStats;
import com.vitaliioleksenko.csp.client.model.user.UserDetailed;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.AuthService;
import com.vitaliioleksenko.csp.client.service.UserService;
import com.vitaliioleksenko.csp.client.util.WindowRenderer;
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
import javafx.stage.Stage;
import lombok.Getter;
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
    @FXML private Button deleteProfileButton;
    @FXML private GridPane userInfoGrid;
    @FXML private VBox userStatsBox;
    @FXML private Label userInfoLabel;
    @FXML private Label tasksCompletedLabel;
    @FXML private Label tasksInProgressLabel;
    @FXML private Label tasksOverdueLabel;
    @Setter private Consumer<Void> userEditCallback;
    @Setter private Consumer<Void> passwordEditCallback;

    private final UserService userService;
    private final AuthService authService;
    private final UserSession userSession;
    private final boolean amIAdmin;
    private Integer userId;

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

    @FXML private void handleEditPassword() {
        if (passwordEditCallback != null) {
            passwordEditCallback.accept(null);
        }
    }

    @FXML private void handleDeleteUser() {
        if (this.userId != null) {
            try {
                userService.deleteUser(this.userId);
                //todo solve cascade delete vs domain integrity problem
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void initData(Integer userId) {
        this.userId = userId;
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
                setupGuestProfileView(amIAdmin, user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void populateUI(UserDetailed user) {
        fullNameLabel.setText(user.getName());
        roleLabel.setText(user.getRole().toString());

        try{
            UserStats stats = userService.getStats(user.getUserId());
            tasksCompletedLabel.setText("Task completed: " + stats.getDoneTasks() );
            tasksOverdueLabel.setText("Task in review: " + stats.getInReviewTasks());
            tasksInProgressLabel.setText("Task in progress: " + stats.getInProgressTasks());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupMyProfileView(UserDetailed me) {
        addRowToGrid(userInfoGrid, "Email", me.getEmail(), 0);
        addRowToGrid(userInfoGrid, "Id", String.valueOf(me.getUserId()), 1);

        editProfileButton.setVisible(true);
        editProfileButton.setManaged(true);
        changePasswordButton.setVisible(true);
        changePasswordButton.setManaged(true);
        deleteProfileButton.setVisible(true);
        deleteProfileButton.setManaged(true);
    }

    private void setupGuestProfileView(boolean amIAdmin, UserDetailed user) {
        if (amIAdmin) {
            setupMyProfileView(user);
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