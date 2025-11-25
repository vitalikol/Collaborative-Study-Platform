package com.vitaliioleksenko.csp.client.controller;

import com.vitaliioleksenko.csp.client.controller.group.GroupCreateController;
import com.vitaliioleksenko.csp.client.controller.group.GroupEditController;
import com.vitaliioleksenko.csp.client.controller.group.GroupProfileController;
import com.vitaliioleksenko.csp.client.controller.group.GroupViewController;
import com.vitaliioleksenko.csp.client.controller.task.TaskCreateController;
import com.vitaliioleksenko.csp.client.controller.task.TaskEditController;
import com.vitaliioleksenko.csp.client.controller.task.TaskProfileController;
import com.vitaliioleksenko.csp.client.controller.task.TaskViewController;
import com.vitaliioleksenko.csp.client.controller.user.UserEditController;
import com.vitaliioleksenko.csp.client.controller.user.UserEditPasswordController;
import com.vitaliioleksenko.csp.client.controller.user.UserProfileController;
import com.vitaliioleksenko.csp.client.controller.user.UserViewController;
import com.vitaliioleksenko.csp.client.model.group.GroupPartial;
import com.vitaliioleksenko.csp.client.model.task.TaskPartial;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.AuthService;
import com.vitaliioleksenko.csp.client.util.WindowRenderer;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.IOException;

public class DashboardController {
    @FXML private BorderPane mainBorderPane;
    @FXML private MenuButton userMenuButton;
    @FXML private Button usersButton;
    @FXML private Button teamButton;
    @FXML private Button taskButton;
    @FXML private Button archiveButton;
    @FXML private Button calendarButton;
    @FXML private Button logsButton;

    private final AuthService authService;
    private final UserSession session;

    public DashboardController() {
        this.authService = new AuthService();
        this.session = UserSession.getInstance();
    }

    @FXML public void initialize() {
        setupRoleBasedUI();
        showActiveTasks();
        userMenuButton.setText(session.getCurrentUser().getEmail());

        Platform.runLater(() -> {
            mainBorderPane.getScene().getRoot().setUserData(this);
        });
    }

    @FXML private void showMyProfile(){
        showUserProfileView(session.getCurrentUserId());
    }

    @FXML private void handleLogOut() {
        try{
            authService.logOut();
            UserSession.getInstance().logout();
            WindowRenderer.switchScene((Stage) userMenuButton.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/auth/login.fxml");
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML private void showMyTeam(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/group/group-view.fxml"));
            Parent view = loader.load();
            GroupViewController controller = loader.getController();
            controller.setNavigationCallback(this::showGroupProfileView);
            controller.setCreateGroupCallback(v -> showGroupCreateView());
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML private void showActiveTasks(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/task/task-view.fxml"));
            Parent view = loader.load();

            TaskViewController controller = loader.getController();

            controller.setViewMode(TaskViewController.TaskViewMode.ACTIVE);
            controller.setNavigationCallback(this::showTaskProfileView);
            controller.setCreateTaskCallback(v -> showTaskCreateView(controller.getSelectedGroup()));

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML private void showTasksArchive(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/task/task-view.fxml"));
            Parent view = loader.load();

            TaskViewController controller = loader.getController();

            controller.setViewMode(TaskViewController.TaskViewMode.ARCHIVE);
            controller.setNavigationCallback(this::showTaskProfileView);

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML private void showLogs(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/logs/log-view.fxml"));
            Parent view = loader.load();
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML private void showUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/user/user-view.fxml"));
            Parent view = loader.load();
            UserViewController controller = loader.getController();
            controller.setNavigationCallback(this::showUserProfileView);

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupRoleBasedUI() {
        teamButton.setText("All group's");
    }


    public void showGroupProfileView(int groupId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/group/group-profile.fxml"));
            Parent view = loader.load();

            GroupProfileController controller = loader.getController();
            controller.initData(groupId);
            controller.setBackNavigationCallback(this::showMyTeam);
            controller.setGroupEditCallback(v -> showGroupEditView(groupId));
            controller.setAddParticipantCallback(v -> showUserSelectionForGroup(groupId));

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showGroupCreateView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/group/group-create.fxml"));
            Parent view = loader.load();

            GroupCreateController controller = loader.getController();

            controller.setCloseCallback(v -> showMyTeam());

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showGroupEditView(int id){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/group/group-edit.fxml"));
            Parent view = loader.load();

            GroupEditController controller = loader.getController();
            controller.initData(id);
            controller.setCloseCallback(v -> showGroupProfileView(id));

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void showTaskProfileView(TaskPartial task) {
        if (task == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/task/task-profile.fxml"));
            Parent view = loader.load();
            TaskProfileController controller = loader.getController();
            controller.initData(task.getTaskId());
            controller.setBackNavigationCallback(this::showActiveTasks);
            controller.setTaskEditCallback(v -> showTaskEditView(task.getTaskId()));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showTaskCreateView(GroupPartial group) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/task/task-create.fxml"));
            Parent view = loader.load();

            TaskCreateController controller = loader.getController();

            controller.setPreSelectedGroup(group);

            controller.setCloseCallback(v -> showActiveTasks());

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showTaskEditView(int id){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/task/task-edit.fxml"));
            Parent view = loader.load();

            TaskEditController controller = loader.getController();
            controller.initData(id);
            controller.setCloseCallback(this::showTaskProfileView);

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void showUserProfileView(Integer userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/user/user-profile.fxml"));
            Parent view = loader.load();
            UserProfileController controller = loader.getController();
            controller.initData(userId);
            controller.setUserEditCallback(v -> showUserEditView(userId));
            controller.setPasswordEditCallback(v -> showUserEditPasswordView(userId));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showUserEditView(int id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/user/user-edit.fxml"));
            Parent view = loader.load();

            UserEditController controller = loader.getController();
            controller.initData(id);
            controller.setCloseCallback(v -> showMyProfile());

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showUserEditPasswordView(int id){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/user/user-edit-password.fxml"));
            Parent view = loader.load();

            UserEditPasswordController controller = loader.getController();
            controller.initData(id);
            controller.setCloseCallback(v -> showMyProfile());

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showUserSelectionForGroup(int groupId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/user/user-view.fxml"));
            Parent view = loader.load();

            UserViewController controller = loader.getController();
            controller.setNavigationCallback(this::showGroupProfileView);
            controller.enableSelectionMode(groupId);

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}