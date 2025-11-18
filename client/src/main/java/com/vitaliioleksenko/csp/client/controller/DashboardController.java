package com.vitaliioleksenko.csp.client.controller;

import com.vitaliioleksenko.csp.client.controller.group.GroupProfileController;
import com.vitaliioleksenko.csp.client.controller.group.GroupViewController;
import com.vitaliioleksenko.csp.client.controller.task.TaskProfileController;
import com.vitaliioleksenko.csp.client.controller.task.TaskViewController;
import com.vitaliioleksenko.csp.client.controller.user.UserProfileController;
import com.vitaliioleksenko.csp.client.controller.user.UserViewController;
import com.vitaliioleksenko.csp.client.model.Task;
import com.vitaliioleksenko.csp.client.util.UserSession;
import com.vitaliioleksenko.csp.client.service.AuthService;
import com.vitaliioleksenko.csp.client.util.WindowRenderer;
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
    private UserSession session = UserSession.getInstance();

    public DashboardController() {
        this.authService = new AuthService();
    }

    @FXML
    public void initialize() {
        setupRoleBasedUI();
        showActiveTasks();
        userMenuButton.setText(session.getCurrentUser().getEmail());
    }

    @FXML
    private void showMyProfile(){
        loadUserProfileView(session.getCurrentUserId());
    }

    @FXML
    private  void showSettings(){
        loadViewToCenter("/com/vitaliioleksenko/csp/client/view/settings/settings.fxml");
    }

    @FXML
    private void handleLogOut() {
        try{
            authService.logOut();
            UserSession.getInstance().logout();
            WindowRenderer.switchScene((Stage) userMenuButton.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/auth/login.fxml");
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void showMyTeam(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/group/group-view.fxml"));
            Parent view = loader.load();

            GroupViewController controller = loader.getController();

            controller.setNavigationCallback(this::showGroupProfileView);

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void showActiveTasks(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/task/task-view.fxml"));
            Parent view = loader.load();

            TaskViewController controller = loader.getController();

            // Використовуємо згенерований Lombok'ом метод setNavigationCallback
            controller.setNavigationCallback(this::showTaskProfileView);

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showTasksArchive(){
        loadViewToCenter("/com/vitaliioleksenko/csp/client/view/task/tasks-archive.fxml");
    }

    @FXML
    private void showCalendar(){
        loadViewToCenter("/com/vitaliioleksenko/csp/client/view/calendar/calendar.fxml");
    }

    @FXML
    private void showLogs(){
        loadViewToCenter("/com/vitaliioleksenko/csp/client/view/logs/logs.fxml");
    }

    @FXML
    private void showUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/user/user-view.fxml"));
            Parent view = loader.load();
            UserViewController controller = loader.getController();
            controller.setNavigationCallback(this::loadUserProfileView);

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupRoleBasedUI() {
        if (session.getCurrentUserRole().equals("ROLE_ADMIN")) {
            usersButton.setVisible(true);
            usersButton.setManaged(true);
            teamButton.setText("Team's");
            calendarButton.setVisible(false);
            calendarButton.setManaged(false);
        }
    }

    public void showTaskProfileView(Task task) {
        if (task == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/task/task-profile.fxml"));
            Parent view = loader.load();

            TaskProfileController controller = loader.getController();

            // Передаємо базовий об'єкт Task у контролер профілю
            controller.initData(task);

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadViewToCenter(String fxmlFileName) {
        try {
            Parent view = WindowRenderer.loadViewInternal(fxmlFileName);
            mainBorderPane.setCenter(view);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadUserProfileView(Integer userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/user/user-profile.fxml"));
            Parent view = loader.load();
            UserProfileController controller = loader.getController();
            controller.initData(userId);
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showGroupProfileView(int groupId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/group/group-profile.fxml"));
            Parent view = loader.load();

            GroupProfileController controller = loader.getController();
            controller.initData(groupId); // Передаємо ID

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
