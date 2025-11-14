package com.vitaliioleksenko.csp.client.controller;

import com.vitaliioleksenko.csp.client.model.Task;
import com.vitaliioleksenko.csp.client.model.User;
import com.vitaliioleksenko.csp.client.service.AuthService;
import com.vitaliioleksenko.csp.client.service.TaskService;
import com.vitaliioleksenko.csp.client.util.WindowRenderer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;


import java.io.IOException;

public class DashboardController {

    @FXML
    private MenuButton userMenuButton;

    @FXML
    private ListView<Task> taskListView;

    private ObservableList<Task> taskData = FXCollections.observableArrayList();
    private AuthService authService;
    private TaskService taskService;
    private User currentUser;

    public DashboardController() {
        this.authService = new AuthService();
        this.taskService = new TaskService();
    }

    @FXML
    public void initialize() throws IOException {
        loadTasks();
        loadUser();

        userMenuButton.setText(currentUser.getEmail());

        taskListView.setItems(taskData);
        taskListView.setCellFactory(listView -> new TaskCell());
    }

    @FXML
    private void handleLogOut() throws IOException {
        authService.logOut();
        WindowRenderer.switchScene((Stage) taskListView.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/login.fxml");
    }

    @FXML
    private void handleProfile(){
        //WindowRenderer.switchScene((Stage) taskListView.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/profile.fxml");
    }

    @FXML
    private  void handleSettings(){
       //WindowRenderer.switchScene((Stage) taskListView.getScene().getWindow(), "/com/vitaliioleksenko/csp/client/view/settings.fxml");
    }

    private void loadTasks() throws IOException {
        taskData.setAll(taskService.getMyTasks());
    }

    private void loadUser(){
        try {
            currentUser = authService.me();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class TaskCell extends ListCell<Task> {
        private Node cardNode;
        private TaskCardController cardController;

        public TaskCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vitaliioleksenko/csp/client/view/task-card.fxml"));
                cardNode = loader.load();
                cardController = loader.getController();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setText(null);
                setGraphic(null);
            } else {
                cardController.setData(task);
                setGraphic(cardNode);
            }
        }
    }
}